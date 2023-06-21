import 'package:flutter/material.dart';
import 'package:flutter_app/widgets/employees_screen.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    Color bgCol = const Color.fromARGB(255, 200, 200, 200);
    return MaterialApp(
      title: 'Employee CRUD Demo',
      theme: ThemeData.light().copyWith(
        useMaterial3: true,
        colorScheme: ColorScheme.fromSeed(
          seedColor: const Color.fromARGB(255, 147, 229, 250),
          brightness: Brightness.light,
          surface: bgCol,
        ),
        scaffoldBackgroundColor: const Color.fromARGB(0, 239, 239, 239),
      ),
      home: const EmployeesScreen(),
    );
  }
}
