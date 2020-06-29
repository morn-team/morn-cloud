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

package site.morn.cloud.admin.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 菜单权限表
 * </p>
 *
 * @author lengleng
 * @since 2019/2/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysMenu extends Model<SysMenu> {

  private static final long serialVersionUID = 1L;

  /**
   * 菜单ID
   */
  @TableId(value = "menu_id", type = IdType.AUTO)
  @ApiModelProperty(value = "菜单id")
  private Integer menuId;
  /**
   * 菜单名称
   */
  @NotBlank(message = "菜单名称不能为空")
  @ApiModelProperty(value = "菜单名称")
  private String name;
  /**
   * 菜单权限标识
   */
  @ApiModelProperty(value = "菜单权限标识")
  private String permission;
  /**
   * 父菜单ID
   */
  @NotNull(message = "菜单父ID不能为空")
  @ApiModelProperty(value = "菜单父id")
  private Integer parentId;
  /**
   * 图标
   */
  @ApiModelProperty(value = "菜单图标")
  private String icon;
  /**
   * 前端URL
   */
  @ApiModelProperty(value = "前端路由标识路径")
  private String path;

  /**
   * 排序值
   */
  @ApiModelProperty(value = "排序值")
  private Integer sort;
  /**
   * 菜单类型 （0菜单 1按钮）
   */
  @NotNull(message = "菜单类型不能为空")
  private String type;
  /**
   * 路由缓冲
   */
  @ApiModelProperty(value = "路由缓冲")
  private String keepAlive;
  /**
   * 创建时间
   */
  @ApiModelProperty(value = "创建时间")
  private LocalDateTime createTime;
  /**
   * 更新时间
   */
  @ApiModelProperty(value = "更新时间")
  private LocalDateTime updateTime;
  /**
   * 0--正常 1--删除
   */
  @TableLogic
  private String delFlag;
}
