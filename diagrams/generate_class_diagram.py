import matplotlib.pyplot as plt
import matplotlib.patches as patches

# Set up figure
fig, ax = plt.subplots(figsize=(16, 11), dpi=300)
ax.set_xlim(0, 16)
ax.set_ylim(0, 11)
ax.axis('off')
fig.patch.set_facecolor('#F8FAFC')

# Colors
HEADER_BLUE = '#1E3A8A'
HEADER_TEAL = '#0D9488'
HEADER_PURPLE = '#6B21A8'
HEADER_AMBER = '#B45309'
HEADER_GREEN = '#15803D'
BG_CARD = '#FFFFFF'
BORDER_COLOR = '#CBD5E1'
TEXT_DARK = '#0F172A'
TEXT_MUTED = '#475569'

def draw_uml_class(ax, x, y, w, h, title, stereotype="", attributes=[], methods=[], header_color=HEADER_BLUE):
    # Shadow
    shadow = patches.FancyBboxPatch(
        (x + 0.08, y - 0.08), w, h,
        boxstyle="round,pad=0.03,rounding_size=0.15",
        facecolor='#94A3B8', alpha=0.25, edgecolor='none'
    )
    ax.add_patch(shadow)

    # Main Card
    box = patches.FancyBboxPatch(
        (x, y), w, h,
        boxstyle="round,pad=0.03,rounding_size=0.15",
        facecolor=BG_CARD, edgecolor=BORDER_COLOR, linewidth=1.5
    )
    ax.add_patch(box)

    # Header Box
    hh = 0.9 if stereotype else 0.65
    header = patches.FancyBboxPatch(
        (x, y + h - hh), w, hh,
        boxstyle="round,pad=0.03,rounding_size=0.15",
        facecolor=header_color, edgecolor='none'
    )
    ax.add_patch(header)

    # Header text
    if stereotype:
        ax.text(x + w/2, y + h - 0.28, f"<<{stereotype}>>", color='#E2E8F0', fontsize=8.5, ha='center', va='center', style='italic')
        ax.text(x + w/2, y + h - 0.62, title, color='#FFFFFF', fontsize=11, fontweight='bold', ha='center', va='center')
    else:
        ax.text(x + w/2, y + h - hh/2, title, color='#FFFFFF', fontsize=11.5, fontweight='bold', ha='center', va='center')

    # Content cursor
    curr_y = y + h - hh - 0.25

    # Attributes
    for attr in attributes:
        ax.text(x + 0.2, curr_y, attr, color=TEXT_DARK, fontsize=8.5, family='monospace', va='center')
        curr_y -= 0.3

    # Divider line if methods exist
    if methods:
        curr_y -= 0.05
        ax.plot([x, x + w], [curr_y, curr_y], color='#E2E8F0', linewidth=1)
        curr_y -= 0.25
        for m in methods:
            ax.text(x + 0.2, curr_y, m, color=TEXT_MUTED, fontsize=8.5, family='monospace', va='center')
            curr_y -= 0.3

# Title banner
ax.text(8.0, 10.5, "ADRIA Bank Microservices Platform — Diagramme de Classes & Modèle Domaine", 
        fontsize=16, fontweight='bold', color='#0F172A', ha='center', va='center')
ax.text(8.0, 10.15, "Architecture Microservices Spring Cloud, Resilience4J, Apache Kafka & Spring AI", 
        fontsize=10.5, color='#64748B', ha='center', va='center')

# --- Module 1: Account Service (Left) ---
draw_uml_class(
    ax, 0.8, 6.2, 4.4, 3.2,
    title="Account",
    stereotype="Entity / Domain",
    header_color=HEADER_BLUE,
    attributes=[
        "- id : UUID [PK]",
        "- customerReference : String",
        "- status : AccountStatus",
        "- currency : String",
        "- balance : double"
    ],
    methods=[
        "+ debit(amount: double, ccy: String) : Account",
        "+ credit(amount: double, ccy: String) : Account",
        "+ transfer(dest: UUID, amt, ccy) : void"
    ]
)

draw_uml_class(
    ax, 0.8, 4.2, 4.4, 1.4,
    title="AccountStatus",
    stereotype="Enumeration",
    header_color=HEADER_BLUE,
    attributes=[
        "  ACTIVE",
        "  BLOCKED"
    ]
)

draw_uml_class(
    ax, 0.8, 1.2, 4.4, 2.4,
    title="AccountService",
    stereotype="Service Interface & Impl",
    header_color='#1E40AF',
    attributes=[
        "- accountRepo : AccountRepository"
    ],
    methods=[
        "+ getAllAccounts() : List<Account>",
        "+ getAccountById(id: UUID) : Account",
        "+ addAccount(acc: Account) : Account",
        "+ debit(id, amount, ccy) : Account",
        "+ credit(id, amount, ccy) : Account",
        "+ transfer(src, dest, amt, ccy) : void"
    ]
)

# --- Module 2: Transaction Service (Center) ---
draw_uml_class(
    ax, 6.0, 6.2, 4.6, 3.2,
    title="Transaction",
    stereotype="Entity / Domain",
    header_color=HEADER_TEAL,
    attributes=[
        "- id : UUID [PK]",
        "- accountId : UUID [FK Logical]",
        "- amount : BigDecimal",
        "- currency : String",
        "- merchantCategory : String",
        "- country : String",
        "- createdAt : Instant"
    ],
    methods=[
        "+ validateAmount(maxAmount: double) : void",
        "+ validateCurrency(accCurrency) : void"
    ]
)

draw_uml_class(
    ax, 6.0, 3.6, 4.6, 2.1,
    title="AccountRestClient",
    stereotype="FeignClient (Resilience4J)",
    header_color='#0F766E',
    attributes=[
        "@CircuitBreaker(name='account-service')"
    ],
    methods=[
        "+ getAccountById(id: UUID) : AccountDto",
        "+ debitAccount(id, amt, ccy) : AccountDto",
        "+ getDefaultAccount(id, err) : AccountDto"
    ]
)

draw_uml_class(
    ax, 6.0, 0.8, 4.6, 2.3,
    title="TransactionService",
    stereotype="Service Interface & Impl",
    header_color='#115E59',
    attributes=[
        "- txRepo : TransactionRepository",
        "- accountClient : AccountRestClient",
        "- kafkaTemplate : KafkaTemplate"
    ],
    methods=[
        "+ getAllTransactions() : List<Transaction>",
        "+ getTransactionById(id: UUID) : Transaction",
        "+ createTransaction(tx) : Transaction"
    ]
)

# --- Module 3: Event & AI (Right) ---
draw_uml_class(
    ax, 11.2, 6.2, 4.2, 3.2,
    title="TransactionCreatedEvent",
    stereotype="Kafka Event DTO",
    header_color=HEADER_PURPLE,
    attributes=[
        "- eventId : UUID",
        "- eventType : String",
        "- transactionId : UUID",
        "- accountId : UUID",
        "- amount : BigDecimal",
        "- currency : String",
        "- occurredAt : Instant"
    ],
    methods=[
        "Topic: bank.transactions.created",
        "Key: accountId"
    ]
)

draw_uml_class(
    ax, 11.2, 3.3, 4.2, 2.4,
    title="EBankAiAgent",
    stereotype="Spring AI Assistant",
    header_color=HEADER_AMBER,
    attributes=[
        "- chatClient : ChatClient",
        "- chatMemory : ChatMemory",
        "- toolCallbackProvider : Provider"
    ],
    methods=[
        "+ chat(msg: String, convId: String) : String",
        "Tools MCP: Account & Transaction"
    ]
)

draw_uml_class(
    ax, 11.2, 0.8, 4.2, 2.0,
    title="TransactionEventConsumer",
    stereotype="KafkaListener",
    header_color='#4C1D95',
    attributes=[
        "@KafkaListener(topics='...created')"
    ],
    methods=[
        "+ onTransactionCreated(event) : void",
        "// Journalise sans modifier les données"
    ]
)

# --- Connectors & Arrows ---
def draw_arrow(ax, start, end, label="", color="#475569", style='->', ls='-'):
    ax.annotate(
        "", xy=end, xytext=start,
        arrowprops=dict(arrowstyle=style, color=color, lw=1.5, ls=ls)
    )
    if label:
        mid_x = (start[0] + end[0]) / 2
        mid_y = (start[1] + end[1]) / 2
        ax.text(mid_x, mid_y + 0.12, label, fontsize=8, color=color, fontweight='bold', ha='center',
                bbox=dict(boxstyle="round,pad=0.2", fc="#F1F5F9", ec="none", alpha=0.9))

# Account -> AccountStatus
draw_arrow(ax, (3.0, 6.2), (3.0, 5.6), label="status", color=HEADER_BLUE)

# AccountService -> Account
draw_arrow(ax, (3.0, 3.6), (3.0, 6.2), label="manages", color=HEADER_BLUE, ls='--')

# TransactionService -> Transaction
draw_arrow(ax, (8.3, 3.1), (8.3, 6.2), label="persists", color=HEADER_TEAL, ls='--')

# TransactionService -> AccountRestClient
draw_arrow(ax, (8.3, 3.1), (8.3, 3.6), color=HEADER_TEAL)

# AccountRestClient -> AccountService (Inter-service Feign Call)
draw_arrow(ax, (6.0, 4.6), (5.2, 2.4), label="Feign + CircuitBreaker", color='#DC2626', style='->', ls='--')

# Transaction -> Account (logical FK)
draw_arrow(ax, (6.0, 7.8), (5.2, 7.8), label="accountId (logical FK)", color='#0284C7', style='->', ls=':')

# TransactionService -> TransactionCreatedEvent (publishes)
draw_arrow(ax, (10.6, 2.0), (13.3, 6.2), label="publishes to Kafka", color=HEADER_PURPLE, style='->', ls='--')

# TransactionCreatedEvent -> TransactionEventConsumer (consumes)
draw_arrow(ax, (13.3, 6.2), (13.3, 2.8), label="consumes", color='#4C1D95', style='->', ls='--')

# EBankAiAgent -> MCP Tools
draw_arrow(ax, (11.2, 4.5), (5.2, 7.0), label="MCP Tools (Account)", color=HEADER_AMBER, style='->', ls=':')
draw_arrow(ax, (11.2, 4.0), (10.6, 7.0), label="MCP Tools (Tx)", color=HEADER_AMBER, style='->', ls=':')

plt.tight_layout()
plt.savefig('/Users/kishyassin/Desktop/cloned_github/test adria/diagrams/class_diagram.png', dpi=300, bbox_inches='tight')
print("Successfully generated /Users/kishyassin/Desktop/cloned_github/test adria/diagrams/class_diagram.png")
