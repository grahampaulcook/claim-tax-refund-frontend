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

package controllers

import javax.inject.Inject

import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import connectors.DataCacheConnector
import controllers.actions._
import config.FrontendAppConfig
import forms.BooleanForm
import identifiers.IsTheAddressInTheUKId
import models.Mode
import utils.{Navigator, UserAnswers}
import views.html.isTheAddressInTheUK

import scala.concurrent.Future

class IsTheAddressInTheUKController @Inject()(appConfig: FrontendAppConfig,
                                         override val messagesApi: MessagesApi,
                                         dataCacheConnector: DataCacheConnector,
                                         navigator: Navigator,
                                         authenticate: AuthAction,
                                         getData: DataRetrievalAction,
                                         requireData: DataRequiredAction) extends FrontendController with I18nSupport {

  def onPageLoad(mode: Mode) = (authenticate andThen getData andThen requireData) {
    implicit request =>
      val preparedForm = request.userAnswers.isTheAddressInTheUK match {
        case None => BooleanForm()
        case Some(value) => BooleanForm().fill(value)
      }
      Ok(isTheAddressInTheUK(appConfig, preparedForm, mode))
  }

  def onSubmit(mode: Mode) = (authenticate andThen getData andThen requireData).async {
    implicit request =>
      BooleanForm().bindFromRequest().fold(
        (formWithErrors: Form[_]) =>
          Future.successful(BadRequest(isTheAddressInTheUK(appConfig, formWithErrors, mode))),
        (value) =>
          dataCacheConnector.save[Boolean](request.externalId, IsTheAddressInTheUKId.toString, value).map(cacheMap =>
            Redirect(navigator.nextPage(IsTheAddressInTheUKId, mode)(new UserAnswers(cacheMap))))
      )
  }
}
