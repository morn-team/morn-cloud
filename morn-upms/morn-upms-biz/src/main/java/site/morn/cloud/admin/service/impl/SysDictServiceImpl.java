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

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import site.morn.cloud.admin.api.entity.SysDict;
import site.morn.cloud.admin.api.entity.SysDictItem;
import site.morn.cloud.admin.mapper.SysDictItemMapper;
import site.morn.cloud.admin.mapper.SysDictMapper;
import site.morn.cloud.admin.service.SysDictService;
import site.morn.cloud.common.core.constant.CacheConstants;
import site.morn.cloud.common.core.constant.enums.DictTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * 字典表
 *
 * @author lengleng
 * @date 2019/03/19
 */
@Service
@RequiredArgsConstructor
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements
    SysDictService {

  private final SysDictItemMapper dictItemMapper;

  /**
   * 根据ID 删除字典
   *
   * @param id 字典ID
   * @return
   */
  @Override
  @CacheEvict(value = CacheConstants.DICT_DETAILS, allEntries = true)
  @Transactional(rollbackFor = Exception.class)
  public void removeDict(Integer id) {
    SysDict dict = this.getById(id);
    // 系统内置
    Assert.state(!DictTypeEnum.SYSTEM.getType().equals(dict.getSystem()), "系统内置字典项目不能删除");
    baseMapper.deleteById(id);
    dictItemMapper.delete(Wrappers.<SysDictItem>lambdaQuery()
        .eq(SysDictItem::getDictId, id));
  }

  /**
   * 更新字典
   *
   * @param dict 字典
   * @return
   */
  @Override
  public void updateDict(SysDict dict) {
    SysDict sysDict = this.getById(dict.getId());
    // 系统内置
    Assert.state(!DictTypeEnum.SYSTEM.getType().equals(sysDict.getSystem()), "系统内置字典项目不能修改");
    this.updateById(dict);
  }
}
