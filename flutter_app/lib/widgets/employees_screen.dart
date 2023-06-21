import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter_app/models/employee.dart';
import 'package:flutter_app/widgets/block.dart';
import 'package:flutter_app/widgets/edit_employee.dart';
import 'package:flutter_app/widgets/employee_tile.dart';
import 'package:flutter_app/widgets/new_employee.dart';
import 'package:http/http.dart' as http;

class EmployeesScreen extends StatefulWidget {
  const EmployeesScreen({super.key});

  @override
  State<StatefulWidget> createState() {
    return _EmployeesScreenState();
  }
}

class _EmployeesScreenState extends State<EmployeesScreen> {
  List<Employee> _employeesList = [];
  String? _error;
  var _isLoading = true;

  @override
  void initState() {
    super.initState();
    _loadEmployees();
  }

  void _loadEmployees() async {
    final url = Uri.http('192.168.0.103:8080', '/employees');

    try {
      final response = await http.get(url);

      if (response.statusCode >= 400) {
        setState(() {
          _error = 'Failed to fetch data. Please try again later.';
        });
      }

      if (response.body == 'null') {
        setState(() {
          _isLoading = false;
        });
      }

      final List<dynamic> listData = json.decode(response.body);

      if (listData.isEmpty) {
        setState(() {
          _isLoading = false;
          _employeesList = List<Employee>.from(listData);
        });
      }

      final List<Employee> loadedEmployees = [];

      for (final employee in listData) {
        loadedEmployees.add(
          Employee(
            id: employee['id'],
            firstName: employee['firstName'],
            lastName: employee['lastName'],
            jobTitle: employee['jobTitle'],
            emailAddress: employee['emailAddress'],
            phoneNumber: employee['phoneNumber'],
            salary: employee['salary'],
          ),
        );

        setState(() {
          _employeesList = loadedEmployees;
          _isLoading = false;
        });
      }
    } catch (error) {
      setState(() {
        _error = 'Something went wrong! Please try again later.';
      });
    }
  }

  void _createEmployee() async {
    final newEmployee = await Navigator.of(context).push<Employee>(
      MaterialPageRoute(
        builder: (ctx) => const NewEmployee(),
      ),
    );

    if (newEmployee == null) {
      return;
    }

    setState(() {
      _employeesList.add(newEmployee);
    });
  }

  void _editEmployee(Employee employee) async {
    final newEmployee = await Navigator.of(context).push<Employee>(
      MaterialPageRoute(
        builder: (ctx) => EditEmployee(
          employee: employee,
        ),
      ),
    );
    final index = _employeesList.indexOf(employee);

    if (newEmployee == null) {
      return;
    }

    setState(() {
      _employeesList[index] = newEmployee;
    });
  }

  void deleteEmployee(Employee employee) async {
    final url = Uri.http('192.168.0.103:8080', '/employees/${employee.id}');
    final index = _employeesList.indexOf(employee);
    setState(() {
      _employeesList.remove(employee);
    });

    try {
      final response = await http.delete(url);

      final responseData = json.decode(response.body);

      print(responseData);

      if (response.statusCode >= 400) {
        setState(() {
          _error = responseData['message'];
          _employeesList.insert(index, employee);
        });
      }
    } catch (error) {
      setState(() {
        _error = error.toString();
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    Widget content = const Expanded(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [Text('No employees found.')],
      ),
    );

    if (_isLoading) {
      content = const Center(
        child: CircularProgressIndicator(),
      );
    }

    if (_employeesList.isNotEmpty) {
      setState(() {
        content = Flexible(
          child: ListView.builder(
            padding: EdgeInsets.zero,
            itemCount: _employeesList.length,
            itemBuilder: (ctx, index) => EmployeeTile(
              index: index,
              employee: _employeesList[index],
              deleteEmployee: deleteEmployee,
              editEmployee: _editEmployee,
            ),
          ),
        );
      });
    }

    if (_error != null) {
      content = Center(
        child: Text(_error!),
      );
    }

    return Container(
      decoration: const BoxDecoration(
          gradient: LinearGradient(
              colors: [Colors.white, Color.fromARGB(255, 216, 216, 216)])),
      child: (Scaffold(
        extendBodyBehindAppBar: true,
        appBar: AppBar(
          centerTitle: true,
          elevation: 0,
          backgroundColor: Colors.transparent,
          title: const Text('Employees'),
        ),
        body: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            const SizedBox(
              height: 100,
            ),
            Block(
              mainAxisAlignment: MainAxisAlignment.center,
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                FilledButton.icon(
                  style: FilledButton.styleFrom(
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(5),
                    ),
                    minimumSize: const Size.fromHeight(50),
                  ),
                  onPressed: _createEmployee,
                  icon: const Icon(Icons.add),
                  label: const Text('Create Employee'),
                ),
              ],
            ),
            content
          ],
        ),
      )),
    );
  }
}
