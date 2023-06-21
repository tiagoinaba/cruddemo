import 'package:flutter_app/auth.dart';
import 'package:flutter_app/widgets/employees_screen.dart';
import 'package:flutter_app/widgets/login_register_screen.dart';
import 'package:flutter/material.dart';

class WidgetTree extends StatefulWidget {
  const WidgetTree({super.key});

  @override
  State<StatefulWidget> createState() {
    return _WidgetTreeState();
  }
}

class _WidgetTreeState extends State<WidgetTree> {
  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return StreamBuilder(
      stream: Auth().authStateChanges,
      builder: (context, snapshot) {
        if (snapshot.hasData) {
          return const EmployeesScreen();
        } else {
          return const LoginScreen();
        }
      },
    );
  }
}
