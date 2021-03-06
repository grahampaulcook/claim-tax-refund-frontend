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

package forms

class NationalInsuranceNumberFormSpec extends FormSpec {

  val errorKeyBlank = "nationalInsuranceNumber.blank"
  val errorKeyInvalid = "nationalInsuranceNumber.invalid"
  val testRegex = """^((?!BG)(?!GB)(?!NK)(?!KN)(?!TN)(?!NT)(?!ZZ)(?:[A-CEGHJ-PR-TW-Z][A-CEGHJ-NPR-TW-Z])(?:\d){6}([A-D]|\s)?)|(\d{2})([a-zA-Z])(\d{5})([a-zA-Z])$"""

  "NationalInsuranceNumber Form" must {

    "bind a string when the standard national insurance number is valid" in {
      val validNino = "AB123456A"
      val form = NationalInsuranceNumberForm(testRegex).bind(Map("value" -> validNino))
      form.get shouldBe "AB123456A"
    }

    "bind a string when the temporary national insurance number is valid" in {
      val form = NationalInsuranceNumberForm(testRegex).bind(Map("value" -> "89A12345A"))
      form.get shouldBe "89A12345A"
    }

    "fail to bind an invalid national insurance number" in {
      val expectedError = error("value", errorKeyInvalid)
      checkForError(NationalInsuranceNumberForm(testRegex), Map("value" -> "invalid"), expectedError)
    }

    "fail to bind a blank value" in {
      val expectedError = error("value", errorKeyBlank)
      checkForError(NationalInsuranceNumberForm(testRegex), Map("value" -> ""), expectedError)
    }

    "fail to bind when value is omitted" in {
      val expectedError = error("value", errorKeyBlank)
      checkForError(NationalInsuranceNumberForm(testRegex), emptyForm, expectedError)
    }
  }
}
