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

package site.morn.cloud.admin.api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import site.morn.cloud.admin.api.dto.UserInfo;
import site.morn.cloud.admin.api.feign.factory.RemoteUserServiceFallbackFactory;
import site.morn.cloud.common.core.constant.SecurityConstants;
import site.morn.cloud.common.core.constant.ServiceNameConstants;
import site.morn.cloud.common.core.util.R;

/**
 * @author lengleng
 * @date 2019/2/1
 */
@FeignClient(contextId = "remoteUserService", value = ServiceNameConstants.UMPS_SERVICE, fallbackFactory = RemoteUserServiceFallbackFactory.class)
public interface RemoteUserService {

  /**
   * UMPS_SERVICE 通过用户名查询用户、角色信息
   *
   * @param username 用户名
   * @param from     调用标志
   * @return R
   */
  @GetMapping("/user/info/{username}")
  R<UserInfo> info(@PathVariable("username") String username
      , @RequestHeader(SecurityConstants.FROM) String from);

  /**
   * 通过社交账号查询用户、角色信息
   *
   * @param inStr appid@code
   * @return
   */
  @GetMapping("/social/info/{inStr}")
  R<UserInfo> social(@PathVariable("inStr") String inStr);
}
