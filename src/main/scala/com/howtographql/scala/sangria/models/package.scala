
package com.howtographql.scala.sangria

import sangria.validation.Violation

package object models{
  case class Link(id: Int, url: String, description: String)

}