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
import site.morn.cloud.admin.api.entity.SysLog;
import site.morn.cloud.admin.service.SysLogService;
import site.morn.cloud.common.core.util.R;
import site.morn.cloud.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 日志表 前端控制器
 * </p>
 *
 * @author lengleng
 * @since 2019/2/1
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/log")
@Api(value = "log", tags = "日志管理模块")
public class LogController {

  private final SysLogService sysLogService;

  /**
   * 简单分页查询
   *
   * @param page   分页对象
   * @param sysLog 系统日志
   * @return
   */
  @GetMapping("/page")
  public R getLogPage(Page page, SysLog sysLog) {
    return R.ok(sysLogService.page(page, Wrappers.query(sysLog)));
  }

  /**
   * 删除日志
   *
   * @param id ID
   * @return success/false
   */
  @DeleteMapping("/{id}")
  @PreAuthorize("@pms.hasPermission('sys_log_del')")
  public R removeById(@PathVariable Long id) {
    return R.ok(sysLogService.removeById(id));
  }

  /**
   * 插入日志
   *
   * @param sysLog 日志实体
   * @return success/false
   */
  @Inner
  @PostMapping
  public R save(@Valid @RequestBody SysLog sysLog) {
    return R.ok(sysLogService.save(sysLog));
  }

}
