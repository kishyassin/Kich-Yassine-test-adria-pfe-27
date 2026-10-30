import 'package:flutter/material.dart';
import '../models/transaction.dart';
import '../services/api_service.dart';

class TransactionsScreen extends StatefulWidget {
  const TransactionsScreen({super.key});

  @override
  State<TransactionsScreen> createState() => _TransactionsScreenState();
}

class _TransactionsScreenState extends State<TransactionsScreen> {
  late Future<List<TransactionModel>> _futureTransactions;

  @override
  void initState() {
    super.initState();
    _refresh();
  }

  void _refresh() {
    setState(() {
      _futureTransactions = ApiService.getTransactions();
    });
  }

  void _showAddTransactionDialog() {
    final accountIdController = TextEditingController();
    final amountController = TextEditingController(text: '150.0');
    final categoryController = TextEditingController(text: 'RESTAURANT');
    final countryController = TextEditingController(text: 'MA');
    String selectedCurrency = 'MAD';

    showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        title: const Text('Nouvelle Transaction'),
        content: SingleChildScrollView(
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              TextField(
                controller: accountIdController,
                decoration: const InputDecoration(
                  labelText: 'Account UUID',
                  hintText: 'Collez l\'UUID du compte',
                ),
              ),
              const SizedBox(height: 12),
              TextField(
                controller: amountController,
                keyboardType: TextInputType.number,
                decoration: const InputDecoration(labelText: 'Montant'),
              ),
              const SizedBox(height: 12),
              DropdownButtonFormField<String>(
                initialValue: selectedCurrency,
                decoration: const InputDecoration(labelText: 'Devise'),
                items: const [
                  DropdownMenuItem(value: 'MAD', child: Text('MAD')),
                  DropdownMenuItem(value: 'EUR', child: Text('EUR')),
                  DropdownMenuItem(value: 'USD', child: Text('USD')),
                ],
                onChanged: (val) {
                  if (val != null) selectedCurrency = val;
                },
              ),
              const SizedBox(height: 12),
              TextField(
                controller: categoryController,
                decoration: const InputDecoration(labelText: 'Catégorie Marchand'),
              ),
              const SizedBox(height: 12),
              TextField(
                controller: countryController,
                decoration: const InputDecoration(labelText: 'Pays (Code ISO)'),
              ),
            ],
          ),
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(ctx),
            child: const Text('Annuler'),
          ),
          ElevatedButton(
            onPressed: () async {
              if (accountIdController.text.trim().isEmpty) return;
              final amount = double.tryParse(amountController.text) ?? 0.0;
              final newTx = TransactionModel(
                id: '',
                accountId: accountIdController.text.trim(),
                amount: amount,
                currency: selectedCurrency,
                merchantCategory: categoryController.text.trim(),
                country: countryController.text.trim(),
                createdAt: '',
              );
              try {
                await ApiService.createTransaction(newTx);
                if (!ctx.mounted) return;
                Navigator.pop(ctx);
                _refresh();
                if (!mounted) return;
                ScaffoldMessenger.of(context).showSnackBar(
                  const SnackBar(content: Text('Transaction enregistrée avec succès !')),
                );
              } catch (e) {
                if (!mounted) return;
                ScaffoldMessenger.of(context).showSnackBar(
                  SnackBar(content: Text('Erreur: $e')),
                );
              }
            },
            child: const Text('Valider'),
          ),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Transactions Bancaires'),
        actions: [
          IconButton(
            icon: const Icon(Icons.refresh),
            onPressed: _refresh,
          ),
        ],
      ),
      floatingActionButton: FloatingActionButton.extended(
        onPressed: _showAddTransactionDialog,
        icon: const Icon(Icons.receipt_long),
        label: const Text('Nouvelle Transaction'),
      ),
      body: FutureBuilder<List<TransactionModel>>(
        future: _futureTransactions,
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(child: CircularProgressIndicator());
          }
          if (snapshot.hasError) {
            return Center(
              child: Padding(
                padding: const EdgeInsets.all(16.0),
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    const Icon(Icons.cloud_off, size: 48, color: Colors.red),
                    const SizedBox(height: 8),
                    Text('Erreur: ${snapshot.error}', textAlign: TextAlign.center),
                    const SizedBox(height: 12),
                    ElevatedButton(onPressed: _refresh, child: const Text('Réessayer')),
                  ],
                ),
              ),
            );
          }
          final transactions = snapshot.data ?? [];
          if (transactions.isEmpty) {
            return const Center(child: Text('Aucune transaction enregistrée.'));
          }
          return RefreshIndicator(
            onRefresh: () async => _refresh(),
            child: ListView.builder(
              padding: const EdgeInsets.only(bottom: 80, top: 8, left: 12, right: 12),
              itemCount: transactions.length,
              itemBuilder: (context, index) {
                final tx = transactions[index];
                return Card(
                  elevation: 2,
                  margin: const EdgeInsets.symmetric(vertical: 6),
                  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                  child: ListTile(
                    leading: CircleAvatar(
                      backgroundColor: Colors.blue.shade50,
                      child: const Icon(Icons.payment, color: Colors.blue),
                    ),
                    title: Text(
                      '${tx.amount.toStringAsFixed(2)} ${tx.currency}',
                      style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 16),
                    ),
                    subtitle: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        const SizedBox(height: 4),
                        Text('${tx.merchantCategory} • ${tx.country}'),
                        if (tx.createdAt.isNotEmpty)
                          Text(
                            tx.createdAt,
                            style: TextStyle(fontSize: 11, color: Colors.grey.shade600),
                          ),
                      ],
                    ),
                    trailing: const Icon(Icons.arrow_forward_ios, size: 14, color: Colors.grey),
                  ),
                );
              },
            ),
          );
        },
      ),
    );
  }
}
