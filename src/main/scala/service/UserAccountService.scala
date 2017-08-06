package service

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration.DurationInt
import models.UserAccount
import scala.concurrent.ExecutionContext.Implicits.global


trait UserAccountService {

  def createAccount(listOfUsers: List[List[String]]) = {

    val system = ActorSystem("UserAccountGeneratorSystem")
    val ref = system.actorOf(AccountGeneratorActor.props)

    implicit val timeout = Timeout(1000 seconds)
    val accountsList = listOfUsers.map(ref ? _)
    print(accountsList)
  }

}

object Major extends App with UserAccountService{

  createAccount(List(List("divya", "mzn", "dd", "1000"), List("divyadua", "mzn", "dd", "1000")))


}
