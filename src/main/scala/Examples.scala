import slick.jdbc.H2Profile.api._

import scala.concurrent.Await
import scala.concurrent.duration._


object AsyncHelper {
  def exec[T](action: DBIO[T])(implicit db: Database): T = Await.result(db.run(action), 2.seconds)
}



object Example1 extends App {
  import AsyncHelper._

  implicit val db = Database.forConfig("h2mem")

  val createTable = sql"CREATE TABLE USERS(ID BIGINT AUTO_INCREMENT, LOGIN VARCHAR(255), PRIMARY KEY(ID))".asUpdate
  def addUser(login: String) = sqlu"INSERT INTO USERS VALUES (null, ${login})"
  val users = sql"SELECT ID, LOGIN FROM USERS".as[(Long, String)]


  exec(createTable)
  exec(addUser("User 1"))
  exec(addUser("User 2"))
  exec(users).foreach(println)
}
