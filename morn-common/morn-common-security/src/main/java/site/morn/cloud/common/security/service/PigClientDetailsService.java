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

package site.morn.cloud.common.security.service;

import site.morn.cloud.common.core.constant.CacheConstants;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;

/**
 * @author lengleng
 * @date 2019/2/1
 * <p>
 * see JdbcClientDetailsService
 */
public class PigClientDetailsService extends JdbcClientDetailsService {

  public PigClientDetailsService(DataSource dataSource) {
    super(dataSource);
  }

  /**
   * 重写原生方法支持redis缓存
   *
   * @param clientId
   * @return
   */
  @Override
  @SneakyThrows
  @Cacheable(value = CacheConstants.CLIENT_DETAILS_KEY, key = "#clientId", unless = "#result == null")
  public ClientDetails loadClientByClientId(String clientId) {
    return super.loadClientByClientId(clientId);
  }
}
