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

package site.morn.cloud.gateway.config;

import site.morn.cloud.gateway.handler.HystrixFallbackHandler;
import site.morn.cloud.gateway.handler.ImageCodeHandler;
import site.morn.cloud.gateway.handler.SwaggerResourceHandler;
import site.morn.cloud.gateway.handler.SwaggerSecurityHandler;
import site.morn.cloud.gateway.handler.SwaggerUiHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

/**
 * @author lengleng
 * @date 2019/2/1 路由配置信息
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class RouterFunctionConfiguration {

  private final HystrixFallbackHandler hystrixFallbackHandler;
  private final ImageCodeHandler imageCodeHandler;
  private final SwaggerResourceHandler swaggerResourceHandler;
  private final SwaggerSecurityHandler swaggerSecurityHandler;
  private final SwaggerUiHandler swaggerUiHandler;


  @Bean
  public RouterFunction routerFunction() {
    return RouterFunctions.route(
        RequestPredicates.path("/fallback")
            .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), hystrixFallbackHandler)
        .andRoute(RequestPredicates.GET("/code")
            .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), imageCodeHandler)
        .andRoute(RequestPredicates.GET("/swagger-resources")
            .and(RequestPredicates.accept(MediaType.ALL)), swaggerResourceHandler)
        .andRoute(RequestPredicates.GET("/swagger-resources/configuration/ui")
            .and(RequestPredicates.accept(MediaType.ALL)), swaggerUiHandler)
        .andRoute(RequestPredicates.GET("/swagger-resources/configuration/security")
            .and(RequestPredicates.accept(MediaType.ALL)), swaggerSecurityHandler);

  }

}
