
package com.howtographql.scala.sangria

import sangria.validation.Violation

package object models{
  case class Link(id: Int, url: String, description: String)

  case class User(id: Int, name: String, email: String, password: String)

}