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

package site.morn.cloud.codegen.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import site.morn.cloud.codegen.entity.GenConfig;
import site.morn.cloud.codegen.entity.GenFormConf;
import site.morn.cloud.codegen.mapper.GenFormConfMapper;
import site.morn.cloud.codegen.mapper.GeneratorMapper;
import site.morn.cloud.codegen.service.GeneratorService;
import site.morn.cloud.codegen.util.CodeGenUtils;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author lengleng
 * @date 2018-07-30
 * <p>
 * 代码生成器
 */
@Service
@RequiredArgsConstructor
public class GeneratorServiceImpl implements GeneratorService {

  private final GeneratorMapper generatorMapper;
  private final GenFormConfMapper genFormConfMapper;

  /**
   * 分页查询表
   *
   * @param tableName 查询条件
   * @param dsName
   * @return
   */
  @Override
  @DS("#last")
  public IPage<List<Map<String, Object>>> getPage(Page page, String tableName, String dsName) {
    return generatorMapper.queryList(page, tableName);
  }

  /**
   * 生成代码
   *
   * @param genConfig 生成配置
   * @return
   */
  @Override
  public byte[] generatorCode(GenConfig genConfig) {
    //根据tableName 查询最新的表单配置
    List<GenFormConf> formConfList = genFormConfMapper
        .selectList(Wrappers.<GenFormConf>lambdaQuery()
            .eq(GenFormConf::getTableName, genConfig.getTableName())
            .orderByDesc(GenFormConf::getCreateTime));

    DynamicDataSourceContextHolder.push(genConfig.getDsName());
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    ZipOutputStream zip = new ZipOutputStream(outputStream);

    String tableNames = genConfig.getTableName();
    for (String tableName : StrUtil.split(tableNames, StrUtil.DASHED)) {
      //查询表信息
      Map<String, String> table = generatorMapper.queryTable(tableName, genConfig.getDsName());
      //查询列信息
      List<Map<String, String>> columns = generatorMapper
          .queryColumns(tableName, genConfig.getDsName());
      //生成代码
      if (CollUtil.isNotEmpty(formConfList)) {
        CodeGenUtils.generatorCode(genConfig, table, columns, zip, formConfList.get(0));
      } else {
        CodeGenUtils.generatorCode(genConfig, table, columns, zip, null);
      }
    }
    IoUtil.close(zip);
    return outputStream.toByteArray();
  }
}
