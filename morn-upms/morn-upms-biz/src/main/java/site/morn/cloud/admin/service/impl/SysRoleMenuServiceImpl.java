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

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import site.morn.cloud.admin.api.entity.SysRoleMenu;
import site.morn.cloud.admin.mapper.SysRoleMenuMapper;
import site.morn.cloud.admin.service.SysRoleMenuService;
import site.morn.cloud.common.core.constant.CacheConstants;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 角色菜单表 服务实现类
 * </p>
 *
 * @author lengleng
 * @since 2019/2/1
 */
@Service
@RequiredArgsConstructor
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements
    SysRoleMenuService {

  private final CacheManager cacheManager;

  /**
   * @param role
   * @param roleId  角色
   * @param menuIds 菜单ID拼成的字符串，每个id之间根据逗号分隔
   * @return
   */
  @Override
  @Transactional(rollbackFor = Exception.class)
  @CacheEvict(value = CacheConstants.MENU_DETAILS, key = "#roleId + '_menu'")
  public Boolean saveRoleMenus(String role, Integer roleId, String menuIds) {
    this.remove(Wrappers.<SysRoleMenu>query().lambda()
        .eq(SysRoleMenu::getRoleId, roleId));

    if (StrUtil.isBlank(menuIds)) {
      return Boolean.TRUE;
    }
    List<SysRoleMenu> roleMenuList = Arrays
        .stream(menuIds.split(","))
        .map(menuId -> {
          SysRoleMenu roleMenu = new SysRoleMenu();
          roleMenu.setRoleId(roleId);
          roleMenu.setMenuId(Integer.valueOf(menuId));
          return roleMenu;
        }).collect(Collectors.toList());

    //清空userinfo
    cacheManager.getCache(CacheConstants.USER_DETAILS).clear();
    return this.saveBatch(roleMenuList);
  }
}
