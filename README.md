# CampsiteReservation

->For this project I have used spring framework for developing the web application, with Hibernate as ORM and mysql as database.

->I have written input validations for not entering empty inputs.

->There are also custom validations for reserving maximun of 3 days and reserving minimum of 1 day ahead of arrival and up to 1 month in advance, if this criteria does not match then custom exception will be thrown with corresponding error message.

->For getting campsite availability for each request instead of making database call, I have made use of cache to improve the performance.

->Also there are Junit test case to test handling of concurrent requests.

