import 'package:flutter/material.dart';
import '../models/account.dart';
import '../services/api_service.dart';

class AccountsScreen extends StatefulWidget {
  const AccountsScreen({super.key});

  @override
  State<AccountsScreen> createState() => _AccountsScreenState();
}

class _AccountsScreenState extends State<AccountsScreen> {
  late Future<List<Account>> _futureAccounts;

  @override
  void initState() {
    super.initState();
    _refresh();
  }

  void _refresh() {
    setState(() {
      _futureAccounts = ApiService.getAccounts();
    });
  }

  void _showAddAccountDialog() {
    final refController = TextEditingController();
    final balanceController = TextEditingController(text: '1000.0');
    String selectedCurrency = 'MAD';

    showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        title: const Text('Nouveau Compte Bancaire'),
        content: SingleChildScrollView(
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              TextField(
                controller: refController,
                decoration: const InputDecoration(
                  labelText: 'Référence Client',
                  hintText: 'ex: CUST-ADRIA-10',
                ),
              ),
              const SizedBox(height: 12),
              DropdownButtonFormField<String>(
                initialValue: selectedCurrency,
                decoration: const InputDecoration(labelText: 'Devise'),
                items: const [
                  DropdownMenuItem(value: 'MAD', child: Text('MAD (Dirham)')),
                  DropdownMenuItem(value: 'EUR', child: Text('EUR (Euro)')),
                  DropdownMenuItem(value: 'USD', child: Text('USD (Dollar)')),
                ],
                onChanged: (val) {
                  if (val != null) selectedCurrency = val;
                },
              ),
              const SizedBox(height: 12),
              TextField(
                controller: balanceController,
                keyboardType: TextInputType.number,
                decoration: const InputDecoration(labelText: 'Solde Initial'),
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
              if (refController.text.trim().isEmpty) return;
              final balance = double.tryParse(balanceController.text) ?? 0.0;
              final newAcc = Account(
                id: '',
                customerReference: refController.text.trim(),
                status: 'ACTIVE',
                currency: selectedCurrency,
                balance: balance,
              );
              try {
                await ApiService.createAccount(newAcc);
                if (!ctx.mounted) return;
                Navigator.pop(ctx);
                _refresh();
                if (!mounted) return;
                ScaffoldMessenger.of(context).showSnackBar(
                  const SnackBar(content: Text('Compte créé avec succès !')),
                );
              } catch (e) {
                if (!mounted) return;
                ScaffoldMessenger.of(context).showSnackBar(
                  SnackBar(content: Text('Erreur: $e')),
                );
              }
            },
            child: const Text('Créer'),
          ),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Comptes Bancaires'),
        actions: [
          IconButton(
            icon: const Icon(Icons.refresh),
            onPressed: _refresh,
          ),
        ],
      ),
      floatingActionButton: FloatingActionButton.extended(
        onPressed: _showAddAccountDialog,
        icon: const Icon(Icons.add),
        label: const Text('Nouveau Compte'),
      ),
      body: FutureBuilder<List<Account>>(
        future: _futureAccounts,
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
          final accounts = snapshot.data ?? [];
          if (accounts.isEmpty) {
            return const Center(child: Text('Aucun compte bancaire trouvé.'));
          }
          return RefreshIndicator(
            onRefresh: () async => _refresh(),
            child: ListView.builder(
              padding: const EdgeInsets.only(bottom: 80, top: 8, left: 12, right: 12),
              itemCount: accounts.length,
              itemBuilder: (context, index) {
                final acc = accounts[index];
                final isActive = acc.status == 'ACTIVE';
                return Card(
                  elevation: 2,
                  margin: const EdgeInsets.symmetric(vertical: 6),
                  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                  child: Padding(
                    padding: const EdgeInsets.all(16.0),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Row(
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: [
                            Text(
                              acc.customerReference,
                              style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 16),
                            ),
                            Container(
                              padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
                              decoration: BoxDecoration(
                                color: isActive ? Colors.green.shade100 : Colors.red.shade100,
                                borderRadius: BorderRadius.circular(8),
                              ),
                              child: Text(
                                acc.status,
                                style: TextStyle(
                                  color: isActive ? Colors.green.shade800 : Colors.red.shade800,
                                  fontWeight: FontWeight.w600,
                                  fontSize: 12,
                                ),
                              ),
                            ),
                          ],
                        ),
                        const SizedBox(height: 10),
                        Text(
                          '${acc.balance.toStringAsFixed(2)} ${acc.currency}',
                          style: TextStyle(
                            fontSize: 22,
                            fontWeight: FontWeight.bold,
                            color: Theme.of(context).primaryColor,
                          ),
                        ),
                        const SizedBox(height: 6),
                        Text(
                          'ID: ${acc.id}',
                          style: TextStyle(fontSize: 11, color: Colors.grey.shade600),
                        ),
                      ],
                    ),
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
