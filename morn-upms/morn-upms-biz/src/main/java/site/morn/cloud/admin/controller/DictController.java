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


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import site.morn.cloud.admin.api.entity.SysDict;
import site.morn.cloud.admin.api.entity.SysDictItem;
import site.morn.cloud.admin.service.SysDictItemService;
import site.morn.cloud.admin.service.SysDictService;
import site.morn.cloud.common.core.constant.CacheConstants;
import site.morn.cloud.common.core.util.R;
import site.morn.cloud.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
 * 字典表 前端控制器
 * </p>
 *
 * @author lengleng
 * @since 2019-03-19
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/dict")
@Api(value = "dict", tags = "字典管理模块")
public class DictController {

  private final SysDictItemService sysDictItemService;
  private final SysDictService sysDictService;

  /**
   * 通过ID查询字典信息
   *
   * @param id ID
   * @return 字典信息
   */
  @GetMapping("/{id}")
  public R getById(@PathVariable Integer id) {
    return R.ok(sysDictService.getById(id));
  }

  /**
   * 分页查询字典信息
   *
   * @param page 分页对象
   * @return 分页对象
   */
  @GetMapping("/page")
  public R<IPage> getDictPage(Page page, SysDict sysDict) {
    return R.ok(sysDictService.page(page, Wrappers.query(sysDict)));
  }

  /**
   * 通过字典类型查找字典
   *
   * @param type 类型
   * @return 同类型字典
   */
  @GetMapping("/type/{type}")
  @Cacheable(value = CacheConstants.DICT_DETAILS, key = "#type")
  public R getDictByType(@PathVariable String type) {
    return R.ok(sysDictItemService.list(Wrappers
        .<SysDictItem>query().lambda()
        .eq(SysDictItem::getType, type)));
  }

  /**
   * 添加字典
   *
   * @param sysDict 字典信息
   * @return success、false
   */
  @SysLog("添加字典")
  @PostMapping
  @PreAuthorize("@pms.hasPermission('sys_dict_add')")
  public R save(@Valid @RequestBody SysDict sysDict) {
    return R.ok(sysDictService.save(sysDict));
  }

  /**
   * 删除字典，并且清除字典缓存
   *
   * @param id ID
   * @return R
   */
  @SysLog("删除字典")
  @DeleteMapping("/{id}")
  @PreAuthorize("@pms.hasPermission('sys_dict_del')")
  public R removeById(@PathVariable Integer id) {
    sysDictService.removeDict(id);
    return R.ok();
  }

  /**
   * 修改字典
   *
   * @param sysDict 字典信息
   * @return success/false
   */
  @PutMapping
  @SysLog("修改字典")
  @PreAuthorize("@pms.hasPermission('sys_dict_edit')")
  public R updateById(@Valid @RequestBody SysDict sysDict) {
    sysDictService.updateDict(sysDict);
    return R.ok();
  }

  /**
   * 分页查询
   *
   * @param page        分页对象
   * @param sysDictItem 字典项
   * @return
   */
  @GetMapping("/item/page")
  public R getSysDictItemPage(Page page, SysDictItem sysDictItem) {
    return R.ok(sysDictItemService.page(page, Wrappers.query(sysDictItem)));
  }


  /**
   * 通过id查询字典项
   *
   * @param id id
   * @return R
   */
  @GetMapping("/item/{id}")
  public R getDictItemById(@PathVariable("id") Integer id) {
    return R.ok(sysDictItemService.getById(id));
  }

  /**
   * 新增字典项
   *
   * @param sysDictItem 字典项
   * @return R
   */
  @SysLog("新增字典项")
  @PostMapping("/item")
  @CacheEvict(value = CacheConstants.DICT_DETAILS, allEntries = true)
  public R save(@RequestBody SysDictItem sysDictItem) {
    return R.ok(sysDictItemService.save(sysDictItem));
  }

  /**
   * 修改字典项
   *
   * @param sysDictItem 字典项
   * @return R
   */
  @SysLog("修改字典项")
  @PutMapping("/item")
  public R updateById(@RequestBody SysDictItem sysDictItem) {
    return sysDictItemService.updateDictItem(sysDictItem);
  }

  /**
   * 通过id删除字典项
   *
   * @param id id
   * @return R
   */
  @SysLog("删除字典项")
  @DeleteMapping("/item/{id}")
  public R removeDictItemById(@PathVariable Integer id) {
    return sysDictItemService.removeDictItem(id);
  }
}
