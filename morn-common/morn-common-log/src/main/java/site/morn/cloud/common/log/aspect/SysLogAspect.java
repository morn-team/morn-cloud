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

package site.morn.cloud.common.log.aspect;

import site.morn.cloud.common.core.util.SpringContextHolder;
import site.morn.cloud.common.log.annotation.SysLog;
import site.morn.cloud.common.log.event.SysLogEvent;
import site.morn.cloud.common.log.util.SysLogUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * 操作日志使用spring event异步入库
 *
 * @author L.cm
 */
@Aspect
@Slf4j
public class SysLogAspect {

  @Around("@annotation(sysLog)")
  @SneakyThrows
  public Object around(ProceedingJoinPoint point, SysLog sysLog) {
    String strClassName = point.getTarget().getClass().getName();
    String strMethodName = point.getSignature().getName();
    log.debug("[类名]:{},[方法]:{}", strClassName, strMethodName);

    site.morn.cloud.admin.api.entity.SysLog logVo = SysLogUtils.getSysLog();
    logVo.setTitle(sysLog.value());
    // 发送异步日志事件
    Long startTime = System.currentTimeMillis();
    Object obj = point.proceed();
    Long endTime = System.currentTimeMillis();
    logVo.setTime(endTime - startTime);
    SpringContextHolder.publishEvent(new SysLogEvent(logVo));
    return obj;
  }

}
