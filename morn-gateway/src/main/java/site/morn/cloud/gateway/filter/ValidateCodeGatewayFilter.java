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

package site.morn.cloud.gateway.filter;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import site.morn.cloud.common.core.constant.CommonConstants;
import site.morn.cloud.common.core.constant.SecurityConstants;
import site.morn.cloud.common.core.exception.ValidateCodeException;
import site.morn.cloud.common.core.util.R;
import site.morn.cloud.common.core.util.WebUtils;
import site.morn.cloud.gateway.config.IgnoreClientConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author lengleng
 * @date 2018/7/4 验证码处理
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ValidateCodeGatewayFilter extends AbstractGatewayFilterFactory {

  private final IgnoreClientConfiguration ignoreClient;
  private final ObjectMapper objectMapper;
  private final RedisTemplate redisTemplate;

  @Override
  public GatewayFilter apply(Object config) {
    return (exchange, chain) -> {
      ServerHttpRequest request = exchange.getRequest();

      // 不是登录请求，直接向下执行
      if (!StrUtil.containsAnyIgnoreCase(request.getURI().getPath()
          , SecurityConstants.OAUTH_TOKEN_URL)) {
        return chain.filter(exchange);
      }

      // 刷新token，直接向下执行
      String grantType = request.getQueryParams().getFirst("grant_type");
      if (StrUtil.equals(SecurityConstants.REFRESH_TOKEN, grantType)) {
        return chain.filter(exchange);
      }

      // 终端设置不校验， 直接向下执行
      try {
        String[] clientInfos = WebUtils.getClientId(request);
        if (ignoreClient.getClients().contains(clientInfos[0])) {
          return chain.filter(exchange);
        }

        //校验验证码
        checkCode(request);
      } catch (Exception e) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.PRECONDITION_REQUIRED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        final String errMsg = e.getMessage();
        return response.writeWith(Mono.create(monoSink -> {
          try {
            byte[] bytes = objectMapper.writeValueAsBytes(R.failed(errMsg));
            DataBuffer dataBuffer = response.bufferFactory().wrap(bytes);

            monoSink.success(dataBuffer);
          } catch (JsonProcessingException jsonProcessingException) {
            log.error("对象输出异常", jsonProcessingException);
            monoSink.error(jsonProcessingException);
          }
        }));
      }

      return chain.filter(exchange);
    };
  }

  /**
   * 检查code
   *
   * @param request
   */
  @SneakyThrows
  private void checkCode(ServerHttpRequest request) {
    String code = request.getQueryParams().getFirst("code");

    if (StrUtil.isBlank(code)) {
      throw new ValidateCodeException("验证码不能为空");
    }

    String randomStr = request.getQueryParams().getFirst("randomStr");
    if (StrUtil.isBlank(randomStr)) {
      randomStr = request.getQueryParams().getFirst("mobile");
    }

    String key = CommonConstants.DEFAULT_CODE_KEY + randomStr;
    if (!redisTemplate.hasKey(key)) {
      throw new ValidateCodeException("验证码不合法");
    }

    Object codeObj = redisTemplate.opsForValue().get(key);

    if (codeObj == null) {
      throw new ValidateCodeException("验证码不合法");
    }

    String saveCode = codeObj.toString();
    if (StrUtil.isBlank(saveCode)) {
      redisTemplate.delete(key);
      throw new ValidateCodeException("验证码不合法");
    }

    if (!StrUtil.equals(saveCode, code)) {
      redisTemplate.delete(key);
      throw new ValidateCodeException("验证码不合法");
    }

    redisTemplate.delete(key);
  }
}
