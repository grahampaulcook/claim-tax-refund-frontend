/*
 * Copyright 2017 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package views

import config.FrontendAppConfig
import play.api.data.Form
import controllers.routes
import forms.FullNameForm
import models.NormalMode
import org.scalatest.mockito.MockitoSugar
import views.behaviours.StringViewBehaviours
import views.html.fullName

class FullNameViewSpec extends StringViewBehaviours with MockitoSugar{

  val maxLength = 35
  val messageKeyPrefix = "fullName"

  val appConfig: FrontendAppConfig = mock[FrontendAppConfig]

  override val form: Form[String] = new FullNameForm(appConfig)()

  def createView = () => fullName(frontendAppConfig, form, NormalMode)(fakeRequest, messages)

  def createViewUsingForm = (form: Form[String]) => fullName(frontendAppConfig, form, NormalMode)(fakeRequest, messages)

  "FullName view" must {
    behave like normalPage(createView, messageKeyPrefix)

    behave like stringPage(createViewUsingForm, messageKeyPrefix, routes.FullNameController.onSubmit(NormalMode).url)
  }
}
