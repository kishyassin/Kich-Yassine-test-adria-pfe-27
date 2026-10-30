class Account {
  final String id;
  final String customerReference;
  final String status;
  final String currency;
  final double balance;

  Account({
    required this.id,
    required this.customerReference,
    required this.status,
    required this.currency,
    required this.balance,
  });

  factory Account.fromJson(Map<String, dynamic> json) {
    return Account(
      id: json['id']?.toString() ?? '',
      customerReference: json['customerReference']?.toString() ?? '',
      status: json['status']?.toString() ?? 'ACTIVE',
      currency: json['currency']?.toString() ?? 'MAD',
      balance: (json['balance'] is num) ? (json['balance'] as num).toDouble() : 0.0,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'customerReference': customerReference,
      'status': status,
      'currency': currency,
      'balance': balance,
    };
  }
}
