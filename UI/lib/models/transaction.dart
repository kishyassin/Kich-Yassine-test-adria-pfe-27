class TransactionModel {
  final String id;
  final String accountId;
  final double amount;
  final String currency;
  final String merchantCategory;
  final String country;
  final String createdAt;

  TransactionModel({
    required this.id,
    required this.accountId,
    required this.amount,
    required this.currency,
    required this.merchantCategory,
    required this.country,
    required this.createdAt,
  });

  factory TransactionModel.fromJson(Map<String, dynamic> json) {
    return TransactionModel(
      id: json['id']?.toString() ?? '',
      accountId: json['accountId']?.toString() ?? '',
      amount: (json['amount'] is num) ? (json['amount'] as num).toDouble() : 0.0,
      currency: json['currency']?.toString() ?? 'MAD',
      merchantCategory: json['merchantCategory']?.toString() ?? 'OTHER',
      country: json['country']?.toString() ?? 'MA',
      createdAt: json['createdAt']?.toString() ?? '',
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'accountId': accountId,
      'amount': amount,
      'currency': currency,
      'merchantCategory': merchantCategory,
      'country': country,
    };
  }
}
