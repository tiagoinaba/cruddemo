import 'dart:convert';

import 'package:email_validator/email_validator.dart';
import 'package:flutter/material.dart';
import 'package:flutter_app/models/employee.dart';
import 'package:flutter_app/widgets/block.dart';
import 'package:http/http.dart' as http;

class EditEmployee extends StatefulWidget {
  EditEmployee({super.key, required this.employee});

  var employee;

  @override
  State<EditEmployee> createState() => _EditEmployeeState();
}

class _EditEmployeeState extends State<EditEmployee> {
  static final RegExp numberRegExp = RegExp(r'^(?:[+0]9)?[0-9]+$');
  static final RegExp salaryRegExp = RegExp(r'^[0-9]+\.?[0-9]*');
  final _formKey = GlobalKey<FormState>();
  var id = '';
  var _firstName = '';
  var _lastName = '';
  var _jobTitle = '';
  var _emailAddress = '';
  var _phoneNumber = '';
  double _salary = 0;
  var _isSending = false;

  void _saveItem() async {
    if (_formKey.currentState!.validate()) {
      _formKey.currentState!.save();
      setState(() {
        _isSending = true;
      });

      final url = Uri.http('192.168.0.103:8080', '/employees');

      final response = await http.put(
        url,
        headers: {
          'Content-Type': 'application/json',
        },
        body: json.encode(
          {
            'id': id,
            'firstName': _firstName,
            'lastName': _lastName,
            'jobTitle': _jobTitle,
            'emailAddress': _emailAddress,
            'phoneNumber': _phoneNumber,
            'salary': _salary,
          },
        ),
      );

      final Map<String, dynamic> resData = json.decode(response.body);

      if (!context.mounted) {
        return;
      }

      Navigator.of(context).pop(
        Employee(
          id: resData['id'],
          firstName: resData['firstName'],
          lastName: resData['lastName'],
          jobTitle: resData['jobTitle'],
          emailAddress: resData['emailAddress'],
          phoneNumber: resData['phoneNumber'],
          salary: resData['salary'],
        ),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    Employee employee = widget.employee;

    setState(() {
      _firstName = employee.firstName;
      _lastName = employee.lastName;
      _jobTitle = employee.jobTitle;
      _emailAddress = employee.emailAddress;
      _phoneNumber = employee.phoneNumber;
      _salary = employee.salary;
    });

    return Container(
      decoration: const BoxDecoration(
        gradient: LinearGradient(
          colors: [
            Colors.white,
            Color.fromARGB(255, 216, 216, 216),
          ],
        ),
      ),
      child: Scaffold(
        resizeToAvoidBottomInset: false,
        appBar: AppBar(
          backgroundColor: Colors.transparent,
          // title: const Text('Create employee'),
        ),
        body: Block(
          mainAxisAlignment: MainAxisAlignment.start,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            const Text('Edit Employee'),
            Form(
              key: _formKey,
              child: Column(
                children: [
                  Row(
                    children: [
                      Flexible(
                        child: TextFormField(
                          maxLength: 50,
                          initialValue: employee.firstName,
                          decoration: const InputDecoration(
                            label: Text('First Name'),
                          ),
                          validator: (value) {
                            if (value == null ||
                                value.isEmpty ||
                                value.trim().length <= 1 ||
                                value.trim().length > 50) {
                              return 'Must be between 1 and 50 characters.';
                            }

                            return null;
                          },
                          onSaved: (value) {
                            id = employee.id;
                            _firstName = value!;
                          },
                        ),
                      ),
                      const SizedBox(
                        width: 5,
                      ),
                      Flexible(
                        child: TextFormField(
                          maxLength: 50,
                          initialValue: employee.lastName,
                          decoration: const InputDecoration(
                            label: Text('Last Name'),
                          ),
                          validator: (value) {
                            if (value == null ||
                                value.isEmpty ||
                                value.trim().length <= 1 ||
                                value.trim().length > 50) {
                              return 'Must be between 1 and 50 characters.';
                            }

                            return null;
                          },
                          onSaved: (value) {
                            _lastName = value!;
                          },
                        ),
                      ),
                    ],
                  ),
                  TextFormField(
                    maxLength: 50,
                    initialValue: employee.jobTitle,
                    decoration: const InputDecoration(
                      label: Text('Job Title'),
                    ),
                    validator: (value) {
                      if (value == null ||
                          value.isEmpty ||
                          value.trim().length <= 1 ||
                          value.trim().length > 50) {
                        return 'Must be between 1 and 50 characters.';
                      }

                      return null;
                    },
                    onSaved: (value) {
                      _jobTitle = value!;
                    },
                  ),
                  TextFormField(
                    maxLength: 70,
                    keyboardType: TextInputType.emailAddress,
                    initialValue: employee.emailAddress,
                    decoration: const InputDecoration(
                      label: Text('Email'),
                    ),
                    validator: (value) {
                      if (!EmailValidator.validate(value!) ||
                          value.isEmpty ||
                          value.trim().length <= 1 ||
                          value.trim().length > 50) {
                        return 'Must be an email address.';
                      }

                      return null;
                    },
                    onSaved: (value) {
                      _emailAddress = value!;
                    },
                  ),
                  TextFormField(
                    maxLength: 20,
                    keyboardType: TextInputType.phone,
                    initialValue: employee.phoneNumber,
                    decoration: const InputDecoration(
                      label: Text('Phone Number'),
                    ),
                    validator: (value) {
                      if (value == null ||
                          value.isEmpty ||
                          value.trim().length <= 1 ||
                          value.trim().length > 20 ||
                          !numberRegExp.hasMatch(value)) {
                        return 'Invalid phone number';
                      }

                      return null;
                    },
                    onSaved: (value) {
                      _phoneNumber = value!;
                    },
                  ),
                  TextFormField(
                    maxLength: 15,
                    keyboardType: TextInputType.number,
                    initialValue: employee.salary.toString(),
                    decoration: const InputDecoration(
                      label: Text('Salary'),
                    ),
                    validator: (value) {
                      if (value == null ||
                          value.isEmpty ||
                          value.trim().length <= 1 ||
                          value.trim().length > 20 ||
                          !salaryRegExp.hasMatch(value)) {
                        return 'Salary has to be a positive double';
                      }

                      return null;
                    },
                    onSaved: (value) {
                      _salary = double.parse(value!);
                    },
                  ),
                  const SizedBox(
                    height: 20,
                  ),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.end,
                    children: [
                      TextButton(
                        onPressed: () {
                          _formKey.currentState!.reset();
                        },
                        child: const Text('Reset'),
                      ),
                      ElevatedButton(
                        onPressed: _saveItem,
                        child: const Text('Send'),
                      ),
                    ],
                  )
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}
