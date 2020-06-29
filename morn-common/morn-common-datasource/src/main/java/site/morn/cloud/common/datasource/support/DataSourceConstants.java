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

package site.morn.cloud.common.datasource.support;

/**
 * @author lengleng
 * @date 2019-04-01
 * <p>
 * 数据源相关常量
 */
public interface DataSourceConstants {

  /**
   * 数据源名称
   */
  String DS_NAME = "name";

  /**
   * 默认驱动
   */
  String DS_DRIVER = "com.mysql.cj.jdbc.Driver";

  /**
   * 默认数据源（master）
   */
  String DS_MASTER = "master";

  /**
   * jdbcurl
   */
  String DS_JDBC_URL = "url";

  /**
   * 用户名
   */
  String DS_USER_NAME = "username";

  /**
   * 密码
   */
  String DS_USER_PWD = "password";

}
