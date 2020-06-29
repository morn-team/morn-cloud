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

package site.morn.cloud.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import site.morn.cloud.admin.api.entity.SysMenu;
import site.morn.cloud.admin.api.vo.MenuVO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 菜单权限表 Mapper 接口
 * </p>
 *
 * @author lengleng
 * @since 2019/2/1
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

  /**
   * 通过角色编号查询菜单
   *
   * @param roleId 角色ID
   * @return
   */
  List<MenuVO> listMenusByRoleId(Integer roleId);

  /**
   * 通过角色ID查询权限
   *
   * @param roleIds Ids
   * @return
   */
  List<String> listPermissionsByRoleIds(String roleIds);
}
