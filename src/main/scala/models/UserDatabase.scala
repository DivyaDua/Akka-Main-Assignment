package models

import java.text.SimpleDateFormat
import java.util.Calendar

import scala.collection.mutable
import scala.collection.mutable.{ListBuffer, Map}

class UserDatabase {

  val userAccountMap: mutable.Map[String, UserAccount] = Map("divya" -> UserAccount(900,"Divya", "Muzaffarnagar", "divya", 100),
   "neha" -> UserAccount(901,"Neha", "Muzaffarnagar", "neha", 1000))

  val accountBillerMap: mutable.Map[Long, ListBuffer[Biller]] = Map(900L -> ListBuffer(Biller("phone", "PhoneBiller", 900), Biller("electricity", "ElectricityBiller", 900)))

  def linkBillers(accountNumber: Long, biller: Biller): mutable.Map[Long, ListBuffer[Biller]] = {

    if (accountBillerMap.contains(accountNumber)) {

      val listOfBillers: ListBuffer[Biller] = getLinkedBillers(accountNumber)
      listOfBillers += biller
      accountBillerMap(accountNumber) = listOfBillers
      println("Updated map " + accountBillerMap)
      accountBillerMap
    }
    else {
      accountBillerMap += (accountNumber -> ListBuffer(biller))
      println("Updated map " + accountBillerMap)
      accountBillerMap
    }
  }


  def getLinkedBillers(accountNumber: Long): ListBuffer[Biller] = {
    if (accountBillerMap.contains(accountNumber))
      accountBillerMap(accountNumber)
    else ListBuffer()
  }

  def depositSalary(accountNumber: Long, accountHolderName: String, salary: Double): Unit = {

    userAccountMap foreach  {
      case (userName, userAccount) =>
        if(userAccount.accountNumber == accountNumber){
          userAccountMap(userName) = userAccount.copy(initialAmount = userAccount.initialAmount + salary)
        }
        else userAccountMap(userName) = userAccount
    }
  }

  def updatedBillers(biller: Biller, billAmount: Double): Biller = {

    val dateFormat = new SimpleDateFormat("d-M-y")
    val currentDate = dateFormat.format(Calendar.getInstance().getTime)

    biller.copy(transactionDate = currentDate, amount = billAmount,
      totalIterations = biller.totalIterations + 1,
      executedIterations = biller.executedIterations + 1, paidAmount = biller.paidAmount + billAmount)

  }

  def payBill(accountNumber: Long, billerCategory: String, billAmount: Double): mutable.Iterable[String] = {

    val result = for {
       (userName, userAccount) <- userAccountMap
        resultString = if (userAccount.accountNumber == accountNumber) {

          if (userAccount.initialAmount > billAmount) {

            userAccountMap(userName) = userAccount.copy(initialAmount = userAccount.initialAmount - billAmount)

            val listOfBillers: ListBuffer[Biller] = getLinkedBillers(accountNumber).filter(_.billerCategory == billerCategory)
            listOfBillers.foreach(getLinkedBillers(accountNumber) -= _)

            val updatedListOfBillers: ListBuffer[Biller] = for (biller <- listOfBillers)
              yield updatedBillers(biller, billAmount)
            updatedListOfBillers.foreach(getLinkedBillers(accountNumber) += _)
            println("Updated map " + userAccountMap + " updated biller " + accountBillerMap)
            "Bill paid"
          }
          else {
            "Insufficient balance"
          }
        }
        else {
          "No such account exists"
        }
    } yield resultString
    result
  }



}
