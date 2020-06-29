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

package site.morn.cloud.admin.service;


import com.baomidou.mybatisplus.extension.service.IService;
import site.morn.cloud.admin.api.dto.MenuTree;
import site.morn.cloud.admin.api.entity.SysMenu;
import site.morn.cloud.admin.api.vo.MenuVO;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 菜单权限表 服务类
 * </p>
 *
 * @author lengleng
 * @since 2019/2/1
 */
public interface SysMenuService extends IService<SysMenu> {

  /**
   * 通过角色编号查询URL 权限
   *
   * @param roleId 角色ID
   * @return 菜单列表
   */
  List<MenuVO> findMenuByRoleId(Integer roleId);

  /**
   * 级联删除菜单
   *
   * @param id 菜单ID
   * @return true成功, false失败
   */
  Boolean removeMenuById(Integer id);

  /**
   * 更新菜单信息
   *
   * @param sysMenu 菜单信息
   * @return 成功、失败
   */
  Boolean updateMenuById(SysMenu sysMenu);

  /**
   * 构建树
   *
   * @param lazy     是否是懒加载
   * @param parentId 父节点ID
   * @return
   */
  List<MenuTree> treeMenu(boolean lazy, Integer parentId);

  /**
   * 查询菜单
   *
   * @param menuSet
   * @param parentId
   * @return
   */
  List<MenuTree> filterMenu(Set<MenuVO> menuSet, Integer parentId);
}
