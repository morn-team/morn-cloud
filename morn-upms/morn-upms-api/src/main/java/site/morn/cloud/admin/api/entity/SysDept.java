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
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 部门管理
 * </p>
 *
 * @author lengleng
 * @since 2019/2/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysDept extends Model<SysDept> {

  private static final long serialVersionUID = 1L;

  @TableId(value = "dept_id", type = IdType.AUTO)
  @ApiModelProperty(value = "部门id")
  private Integer deptId;
  /**
   * 部门名称
   */
  @NotBlank(message = "部门名称不能为空")
  @ApiModelProperty(value = "部门名称")
  private String name;
  /**
   * 排序
   */
  @ApiModelProperty(value = "排序值")
  private Integer sort;
  /**
   * 创建时间
   */
  @ApiModelProperty(value = "创建时间")
  private LocalDateTime createTime;
  /**
   * 修改时间
   */
  @ApiModelProperty(value = "修改时间")
  private LocalDateTime updateTime;

  /**
   * 父级部门id
   */
  @ApiModelProperty(value = "父级部门id")
  private Integer parentId;

  /**
   * 是否删除  -1：已删除  0：正常
   */
  @TableLogic
  private String delFlag;

}
