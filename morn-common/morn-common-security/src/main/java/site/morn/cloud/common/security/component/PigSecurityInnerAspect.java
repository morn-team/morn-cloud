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

package site.morn.cloud.common.security.component;

import cn.hutool.core.util.StrUtil;
import site.morn.cloud.common.core.constant.SecurityConstants;
import site.morn.cloud.common.security.annotation.Inner;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.security.access.AccessDeniedException;

/**
 * @author lengleng
 * @date 2019/02/14
 * <p>
 * 服务间接口不鉴权处理逻辑
 */
@Slf4j
@Aspect
@RequiredArgsConstructor
public class PigSecurityInnerAspect implements Ordered {

  private final HttpServletRequest request;

  @SneakyThrows
  @Around("@annotation(inner)")
  public Object around(ProceedingJoinPoint point, Inner inner) {
    String header = request.getHeader(SecurityConstants.FROM);
    if (inner.value() && !StrUtil.equals(SecurityConstants.FROM_IN, header)) {
      log.warn("访问接口 {} 没有权限", point.getSignature().getName());
      throw new AccessDeniedException("Access is denied");
    }
    return point.proceed();
  }

  @Override
  public int getOrder() {
    return Ordered.HIGHEST_PRECEDENCE + 1;
  }
}
