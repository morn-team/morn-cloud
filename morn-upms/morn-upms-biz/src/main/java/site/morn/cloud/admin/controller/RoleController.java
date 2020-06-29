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
import site.morn.cloud.admin.api.entity.SysRole;
import site.morn.cloud.admin.api.vo.RoleVo;
import site.morn.cloud.admin.service.SysRoleMenuService;
import site.morn.cloud.admin.service.SysRoleService;
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
 * @author lengleng
 * @date 2019/2/1
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/role")
@Api(value = "role", tags = "角色管理模块")
public class RoleController {

  private final SysRoleService sysRoleService;
  private final SysRoleMenuService sysRoleMenuService;

  /**
   * 通过ID查询角色信息
   *
   * @param id ID
   * @return 角色信息
   */
  @GetMapping("/{id}")
  public R getById(@PathVariable Integer id) {
    return R.ok(sysRoleService.getById(id));
  }

  /**
   * 添加角色
   *
   * @param sysRole 角色信息
   * @return success、false
   */
  @SysLog("添加角色")
  @PostMapping
  @PreAuthorize("@pms.hasPermission('sys_role_add')")
  public R save(@Valid @RequestBody SysRole sysRole) {
    return R.ok(sysRoleService.save(sysRole));
  }

  /**
   * 修改角色
   *
   * @param sysRole 角色信息
   * @return success/false
   */
  @SysLog("修改角色")
  @PutMapping
  @PreAuthorize("@pms.hasPermission('sys_role_edit')")
  public R update(@Valid @RequestBody SysRole sysRole) {
    return R.ok(sysRoleService.updateById(sysRole));
  }

  /**
   * 删除角色
   *
   * @param id
   * @return
   */
  @SysLog("删除角色")
  @DeleteMapping("/{id}")
  @PreAuthorize("@pms.hasPermission('sys_role_del')")
  public R removeById(@PathVariable Integer id) {
    return R.ok(sysRoleService.removeRoleById(id));
  }

  /**
   * 获取角色列表
   *
   * @return 角色列表
   */
  @GetMapping("/list")
  public R listRoles() {
    return R.ok(sysRoleService.list(Wrappers.emptyWrapper()));
  }

  /**
   * 分页查询角色信息
   *
   * @param page 分页对象
   * @return 分页对象
   */
  @GetMapping("/page")
  public R getRolePage(Page page) {
    return R.ok(sysRoleService.page(page, Wrappers.emptyWrapper()));
  }

  /**
   * 更新角色菜单
   *
   * @param roleVo 角色对象
   * @return success、false
   */
  @SysLog("更新角色菜单")
  @PutMapping("/menu")
  @PreAuthorize("@pms.hasPermission('sys_role_perm')")
  public R saveRoleMenus(@RequestBody RoleVo roleVo) {
    SysRole sysRole = sysRoleService.getById(roleVo.getRoleId());
    return R.ok(sysRoleMenuService.saveRoleMenus(sysRole.getRoleCode()
        , roleVo.getRoleId(), roleVo.getMenuIds()));
  }
}
