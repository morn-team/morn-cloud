/*
 *
 *  *  Copyright (c) 2019-2020, 冷冷 (wangiegie@gmail.com).
 *  *  <p>
 *  *  Licensed under the GNU Lesser General Public License 3.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  *  You may obtain a copy of the License at
 *  *  <p>
 *  * https://www.gnu.org/licenses/lgpl.html
 *  *  <p>
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package site.morn.cloud.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import site.morn.cloud.admin.api.dto.MenuTree;
import site.morn.cloud.admin.api.entity.SysMenu;
import site.morn.cloud.admin.api.entity.SysRoleMenu;
import site.morn.cloud.admin.api.vo.MenuVO;
import site.morn.cloud.admin.api.vo.TreeUtil;
import site.morn.cloud.admin.mapper.SysMenuMapper;
import site.morn.cloud.admin.mapper.SysRoleMenuMapper;
import site.morn.cloud.admin.service.SysMenuService;
import site.morn.cloud.common.core.constant.CacheConstants;
import site.morn.cloud.common.core.constant.CommonConstants;
import site.morn.cloud.common.core.constant.enums.MenuTypeEnum;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * <p>
 * 菜单权限表 服务实现类
 * </p>
 *
 * @author lengleng
 * @since 2017-10-29
 */
@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements
    SysMenuService {

  private final SysRoleMenuMapper sysRoleMenuMapper;

  @Override
  @Cacheable(value = CacheConstants.MENU_DETAILS, key = "#roleId  + '_menu'", unless = "#result == null")
  public List<MenuVO> findMenuByRoleId(Integer roleId) {
    return baseMapper.listMenusByRoleId(roleId);
  }

  /**
   * 级联删除菜单
   *
   * @param id 菜单ID
   * @return true成功, false失败
   */
  @Override
  @Transactional(rollbackFor = Exception.class)
  @CacheEvict(value = CacheConstants.MENU_DETAILS, allEntries = true)
  public Boolean removeMenuById(Integer id) {
    // 查询父节点为当前节点的节点
    List<SysMenu> menuList = this.list(Wrappers.<SysMenu>query()
        .lambda().eq(SysMenu::getParentId, id));
    Assert.isNull(menuList, "菜单含有下级不能删除");

    sysRoleMenuMapper.delete(Wrappers.<SysRoleMenu>query()
        .lambda().eq(SysRoleMenu::getMenuId, id));
    //删除当前菜单及其子菜单
    return this.removeById(id);
  }

  @Override
  @CacheEvict(value = CacheConstants.MENU_DETAILS, allEntries = true)
  public Boolean updateMenuById(SysMenu sysMenu) {
    return this.updateById(sysMenu);
  }

  /**
   * 构建树查询 1. 不是懒加载情况，查询全部 2. 是懒加载，根据parentId 查询 2.1 父节点为空，则查询ID -1
   *
   * @param lazy     是否是懒加载
   * @param parentId 父节点ID
   * @return
   */
  @Override
  public List<MenuTree> treeMenu(boolean lazy, Integer parentId) {
    if (!lazy) {
      return TreeUtil.buildTree(baseMapper.selectList(Wrappers.<SysMenu>lambdaQuery()
          .orderByAsc(SysMenu::getSort)), CommonConstants.MENU_TREE_ROOT_ID);
    }

    Integer parent = parentId == null ? CommonConstants.MENU_TREE_ROOT_ID : parentId;
    return TreeUtil.buildTree(baseMapper
        .selectList(Wrappers.<SysMenu>lambdaQuery().eq(SysMenu::getParentId, parent)
            .orderByAsc(SysMenu::getSort)), parent);
  }

  /**
   * 查询菜单
   *
   * @param all      全部菜单
   * @param parentId 父节点ID
   * @return
   */
  @Override
  public List<MenuTree> filterMenu(Set<MenuVO> all, Integer parentId) {
    List<MenuTree> menuTreeList = all.stream()
        .filter(vo -> MenuTypeEnum.LEFT_MENU.getType().equals(vo.getType()))
        .map(MenuTree::new)
        .sorted(Comparator.comparingInt(MenuTree::getSort))
        .collect(Collectors.toList());
    Integer parent = parentId == null ? CommonConstants.MENU_TREE_ROOT_ID : parentId;
    return TreeUtil.build(menuTreeList, parent);
  }
}
