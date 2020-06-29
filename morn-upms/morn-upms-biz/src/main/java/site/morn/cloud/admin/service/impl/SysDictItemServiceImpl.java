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

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import site.morn.cloud.admin.api.entity.SysDict;
import site.morn.cloud.admin.api.entity.SysDictItem;
import site.morn.cloud.admin.mapper.SysDictItemMapper;
import site.morn.cloud.admin.service.SysDictItemService;
import site.morn.cloud.admin.service.SysDictService;
import site.morn.cloud.common.core.constant.CacheConstants;
import site.morn.cloud.common.core.constant.enums.DictTypeEnum;
import site.morn.cloud.common.core.util.R;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * 字典项
 *
 * @author lengleng
 * @date 2019/03/19
 */
@Service
@RequiredArgsConstructor
public class SysDictItemServiceImpl extends ServiceImpl<SysDictItemMapper, SysDictItem> implements
    SysDictItemService {

  private final SysDictService dictService;

  /**
   * 删除字典项
   *
   * @param id 字典项ID
   * @return
   */
  @Override
  @CacheEvict(value = CacheConstants.DICT_DETAILS, allEntries = true)
  public R removeDictItem(Integer id) {
    //根据ID查询字典ID
    SysDictItem dictItem = this.getById(id);
    SysDict dict = dictService.getById(dictItem.getDictId());
    // 系统内置
    Assert.state(!DictTypeEnum.SYSTEM.getType().equals(dict.getSystem()), "系统内置字典项目不能删除");
    return R.ok(this.removeById(id));
  }

  /**
   * 更新字典项
   *
   * @param item 字典项
   * @return
   */
  @Override
  @CacheEvict(value = CacheConstants.DICT_DETAILS, allEntries = true)
  public R updateDictItem(SysDictItem item) {
    //查询字典
    SysDict dict = dictService.getById(item.getDictId());
    // 系统内置
    Assert.state(!DictTypeEnum.SYSTEM.getType().equals(dict.getSystem()), "系统内置字典项目不能修改");
    return R.ok(this.updateById(item));
  }
}
