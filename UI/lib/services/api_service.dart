import 'dart:convert';
import 'package:http/http.dart' as http;
import '../models/account.dart';
import '../models/transaction.dart';

class ApiService {
  // Support both Gateway and direct microservices
  static String gatewayBaseUrl = 'http://localhost:8888';
  static String accountBaseUrl = 'http://localhost:8082';
  static String transactionBaseUrl = 'http://localhost:8084';
  static String chatbotBaseUrl = 'http://localhost:8083';

  // Helper method to try gateway first, then direct service
  static Future<http.Response> _getWithFallback(String gatewayPath, String directUrl) async {
    try {
      final res = await http.get(Uri.parse('$gatewayBaseUrl$gatewayPath')).timeout(const Duration(seconds: 2));
      if (res.statusCode >= 200 && res.statusCode < 300) return res;
    } catch (_) {}
    return await http.get(Uri.parse(directUrl)).timeout(const Duration(seconds: 4));
  }

  static Future<http.Response> _postWithFallback(String gatewayPath, String directUrl, {Object? body}) async {
    final headers = {'Content-Type': 'application/json'};
    try {
      final res = await http.post(Uri.parse('$gatewayBaseUrl$gatewayPath'), headers: headers, body: body)
          .timeout(const Duration(seconds: 2));
      if (res.statusCode >= 200 && res.statusCode < 300) return res;
    } catch (_) {}
    return await http.post(Uri.parse(directUrl), headers: headers, body: body).timeout(const Duration(seconds: 4));
  }

  // Fetch all accounts
  static Future<List<Account>> getAccounts() async {
    final response = await _getWithFallback('/account-service/api/accounts', '$accountBaseUrl/api/accounts');
    if (response.statusCode == 200) {
      final List<dynamic> data = jsonDecode(response.body);
      return data.map((item) => Account.fromJson(item)).toList();
    } else {
      throw Exception('Erreur chargement comptes (${response.statusCode})');
    }
  }

  // Create account
  static Future<Account> createAccount(Account account) async {
    final response = await _postWithFallback(
      '/account-service/api/accounts',
      '$accountBaseUrl/api/accounts',
      body: jsonEncode(account.toJson()),
    );
    if (response.statusCode == 200 || response.statusCode == 201) {
      return Account.fromJson(jsonDecode(response.body));
    } else {
      throw Exception('Erreur création compte (${response.statusCode})');
    }
  }

  // Fetch all transactions
  static Future<List<TransactionModel>> getTransactions() async {
    final response = await _getWithFallback('/transaction-service/api/transactions', '$transactionBaseUrl/api/transactions');
    if (response.statusCode == 200) {
      final List<dynamic> data = jsonDecode(response.body);
      return data.map((item) => TransactionModel.fromJson(item)).toList();
    } else {
      throw Exception('Erreur chargement transactions (${response.statusCode})');
    }
  }

  // Create transaction
  static Future<TransactionModel> createTransaction(TransactionModel tx) async {
    final response = await _postWithFallback(
      '/transaction-service/api/transactions',
      '$transactionBaseUrl/api/transactions',
      body: jsonEncode(tx.toJson()),
    );
    if (response.statusCode == 200 || response.statusCode == 201) {
      return TransactionModel.fromJson(jsonDecode(response.body));
    } else {
      throw Exception('Erreur création transaction (${response.statusCode}): ${response.body}');
    }
  }

  // Send message to AI Chatbot
  static Future<String> askChatbot(String message, {String conversationId = 'default'}) async {
    final query = Uri.encodeComponent(message);
    final conv = Uri.encodeComponent(conversationId);
    final response = await _getWithFallback(
      '/ebank-chatbot/chat?message=$query&conversationId=$conv',
      '$chatbotBaseUrl/chat?message=$query&conversationId=$conv',
    );
    if (response.statusCode == 200) {
      return response.body;
    } else {
      throw Exception('Erreur chatbot (${response.statusCode})');
    }
  }
}
