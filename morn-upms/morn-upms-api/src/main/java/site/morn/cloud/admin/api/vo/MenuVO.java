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

package site.morn.cloud.admin.api.vo;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * <p>
 * 菜单权限表
 * </p>
 *
 * @author lengleng
 * @since 2017-11-08
 */
@Data
public class MenuVO implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 菜单ID
   */
  private Integer menuId;
  /**
   * 菜单名称
   */
  private String name;
  /**
   * 菜单权限标识
   */
  private String permission;
  /**
   * 父菜单ID
   */
  private Integer parentId;
  /**
   * 图标
   */
  private String icon;
  /**
   * 前端路由标识路径
   */
  private String path;
  /**
   * 排序值
   */
  private Integer sort;
  /**
   * 菜单类型 （0菜单 1按钮）
   */
  private String type;
  /**
   * 是否缓冲
   */
  private String keepAlive;
  /**
   * 创建时间
   */
  private LocalDateTime createTime;
  /**
   * 更新时间
   */
  private LocalDateTime updateTime;
  /**
   * 0--正常 1--删除
   */
  private String delFlag;


  @Override
  public int hashCode() {
    return menuId.hashCode();
  }

  /**
   * menuId 相同则相同
   *
   * @param obj
   * @return
   */
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof MenuVO) {
      Integer targetMenuId = ((MenuVO) obj).getMenuId();
      return menuId.equals(targetMenuId);
    }
    return super.equals(obj);
  }
}
