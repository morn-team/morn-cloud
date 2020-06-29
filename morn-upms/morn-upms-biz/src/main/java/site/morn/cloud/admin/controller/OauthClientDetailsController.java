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

package site.morn.cloud.admin.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import site.morn.cloud.admin.api.entity.SysOauthClientDetails;
import site.morn.cloud.admin.service.SysOauthClientDetailsService;
import site.morn.cloud.common.core.util.R;
import site.morn.cloud.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lengleng
 * @since 2018-05-15
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/client")
@Api(value = "client", tags = "客户端管理模块")
public class OauthClientDetailsController {

  private final SysOauthClientDetailsService sysOauthClientDetailsService;

  /**
   * 通过ID查询
   *
   * @param id clientId
   * @return SysOauthClientDetails
   */
  @GetMapping("/{id}")
  public R getById(@PathVariable String id) {
    return R.ok(sysOauthClientDetailsService.getById(id));
  }


  /**
   * 简单分页查询
   *
   * @param page                  分页对象
   * @param sysOauthClientDetails 系统终端
   * @return
   */
  @GetMapping("/page")
  public R getOauthClientDetailsPage(Page page, SysOauthClientDetails sysOauthClientDetails) {
    return R.ok(sysOauthClientDetailsService.page(page, Wrappers.query(sysOauthClientDetails)));
  }

  /**
   * 添加
   *
   * @param sysOauthClientDetails 实体
   * @return success/false
   */
  @SysLog("添加终端")
  @PostMapping
  @PreAuthorize("@pms.hasPermission('sys_client_add')")
  public R add(@Valid @RequestBody SysOauthClientDetails sysOauthClientDetails) {
    return R.ok(sysOauthClientDetailsService.save(sysOauthClientDetails));
  }

  /**
   * 删除
   *
   * @param id ID
   * @return success/false
   */
  @SysLog("删除终端")
  @DeleteMapping("/{id}")
  @PreAuthorize("@pms.hasPermission('sys_client_del')")
  public R removeById(@PathVariable String id) {
    return R.ok(sysOauthClientDetailsService.removeClientDetailsById(id));
  }

  /**
   * 编辑
   *
   * @param sysOauthClientDetails 实体
   * @return success/false
   */
  @SysLog("编辑终端")
  @PutMapping
  @PreAuthorize("@pms.hasPermission('sys_client_edit')")
  public R update(@Valid @RequestBody SysOauthClientDetails sysOauthClientDetails) {
    return R.ok(sysOauthClientDetailsService.updateClientDetailsById(sysOauthClientDetails));
  }
}
