import 'package:flutter_test/flutter_test.dart';
import 'package:ebank_mobile/main.dart';

void main() {
  testWidgets('EBankApp smoke test', (WidgetTester tester) async {
    await tester.pumpWidget(const EBankApp());
    expect(find.text('Comptes'), findsWidgets);
    expect(find.text('Transactions'), findsWidgets);
    expect(find.text('Assistant IA'), findsWidgets);
  });
}
