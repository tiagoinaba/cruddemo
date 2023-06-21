import 'package:flutter/material.dart';
import 'package:flutter_app/models/employee.dart';
import 'package:flutter_app/widgets/block.dart';
import 'package:flutter_app/widgets/edit_employee.dart';
import 'package:google_fonts/google_fonts.dart';

class EmployeeTile extends StatelessWidget {
  const EmployeeTile({
    super.key,
    required this.index,
    required this.employee,
    required this.deleteEmployee,
    required this.editEmployee,
  });

  final int index;
  final Employee employee;
  final void Function(Employee) editEmployee;
  final void Function(Employee) deleteEmployee;

  @override
  Widget build(BuildContext context) {
    return Block(
      mainAxisAlignment: MainAxisAlignment.center,
      crossAxisAlignment: CrossAxisAlignment.center,
      children: [
        Padding(
          padding: const EdgeInsets.symmetric(horizontal: 8),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Text(
                    '${employee.firstName} ${employee.lastName}',
                    style: GoogleFonts.inter(
                      fontWeight: FontWeight.bold,
                      fontSize: 20,
                    ),
                  ),
                  Row(
                    children: [
                      IconButton(
                        onPressed: () {
                          editEmployee(employee);
                        },
                        icon: const Icon(Icons.edit),
                      ),
                      IconButton(
                          onPressed: () {
                            deleteEmployee(employee);
                          },
                          icon: const Icon(Icons.delete))
                    ],
                  ),
                ],
              ),
              const SizedBox(
                height: 5,
              ),
              Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    'Name',
                    style: GoogleFonts.lato(
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                  Text(
                    '${employee.firstName} ${employee.lastName}',
                  ),
                  const SizedBox(
                    height: 5,
                  ),
                  Text(
                    'Job Title',
                    style: GoogleFonts.lato(
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                  Text(
                    employee.jobTitle,
                  ),
                  const SizedBox(
                    height: 5,
                  ),
                  Text(
                    'Email',
                    style: GoogleFonts.lato(
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                  Text(
                    employee.emailAddress,
                  ),
                  const SizedBox(
                    height: 5,
                  ),
                  Text(
                    'Phone Number',
                    style: GoogleFonts.lato(
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                  Text(
                    employee.phoneNumber,
                  ),
                  const SizedBox(
                    height: 5,
                  ),
                  Text(
                    'Salary',
                    style: GoogleFonts.lato(
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                  Text(
                    (employee.salary).toString(),
                  ),
                ],
              )
            ],
          ),
        ),
      ],
    );
  }
}
