class Employee {
  const Employee({
    required this.id,
    required this.firstName,
    required this.lastName,
    required this.jobTitle,
    required this.emailAddress,
    required this.phoneNumber,
    required this.salary,
  });

  final String id;
  final String firstName;
  final String lastName;
  final String jobTitle;
  final String emailAddress;
  final String phoneNumber;
  final double salary;
}
