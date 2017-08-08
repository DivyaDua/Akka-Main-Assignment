package models

import java.text.SimpleDateFormat
import java.util.Calendar

import scala.collection.mutable
import scala.collection.mutable.{ListBuffer, Map}

class UserDatabase {

  val userAccountMap: mutable.Map[String, UserAccount] = mutable.Map("divya" -> UserAccount(900,"Divya", "Muzaffarnagar", "divya", 100),
   "neha" -> UserAccount(901,"Neha", "Muzaffarnagar", "neha", 1000))

  val accountBillerMap: mutable.Map[Long, ListBuffer[Biller]] = mutable.Map(900L -> ListBuffer(Biller("phone", "PhoneBiller", 900),
    Biller("electricity", "ElectricityBiller", 900)))

  def addAccount(userName: String, userAccount: UserAccount): mutable.Map[String, UserAccount] = {
    userAccountMap += (userName -> userAccount)
    userAccountMap
  }

  def linkBillers(accountNumber: Long, biller: Biller): mutable.Map[Long, ListBuffer[Biller]] = {

    if (accountBillerMap.contains(accountNumber)) {

      val listOfBillers: ListBuffer[Biller] = getLinkedBillers(accountNumber)
      listOfBillers += biller
      accountBillerMap(accountNumber) = listOfBillers
      accountBillerMap
    }
    else {
      accountBillerMap += (accountNumber -> ListBuffer(biller))
      accountBillerMap
    }
  }


  def getLinkedBillers(accountNumber: Long): ListBuffer[Biller] = {
    if (accountBillerMap.contains(accountNumber)) {
      accountBillerMap(accountNumber)
    }
    else {
      ListBuffer()
    }
  }

  def depositSalary(accountNumber: Long, accountHolderName: String, salary: Double): Boolean = {

    val result =  for {(userName, userAccount) <- userAccountMap
        bool = if(userAccount.accountNumber == accountNumber){
          userAccountMap(userName) = userAccount.copy(initialAmount = userAccount.initialAmount + salary)
          true
        }
        else {
          userAccountMap(userName) = userAccount
          false
        }
    } yield (userAccount.accountNumber, bool)
    if(result.contains(accountNumber)){
      result(accountNumber)
    }
    else{
      false
    }
  }

  def updatedBillers(biller: Biller, billAmount: Double): Biller = {

    val dateFormat = new SimpleDateFormat("d-M-y")
    val currentDate = dateFormat.format(Calendar.getInstance().getTime)

    biller.copy(transactionDate = currentDate, amount = billAmount,
      totalIterations = biller.totalIterations + 1,
      executedIterations = biller.executedIterations + 1, paidAmount = biller.paidAmount + billAmount)

  }

  def payBill(accountNumber: Long, billAmount: Double, billerCategory: String): String = {

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
            "Bill paid"
          }
          else {
            "Insufficient balance"
          }
        }
        else {
          "Account does not match"
        }
    } yield (userAccount.accountNumber,resultString)

    if(result.contains(accountNumber)) {
      result(accountNumber)
    }
    else{
      "No such account exists"
    }
  }



}
