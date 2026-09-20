# Class Diagram

```mermaid
classDiagram
    Main --> FeastController
    FeastController --> ConsoleView
    FeastController --> CustomerService
    FeastController --> MenuService
    FeastController --> OrderService
    CustomerService --> CustomerRepository
    MenuService --> MenuRepository
    OrderService --> OrderRepository
    CustomerService o-- Customer
    MenuService o-- FeastMenu
    OrderService o-- FeastOrder
    class Customer { +code +name +phoneNumber +email }
    class FeastMenu { +code +name +price +ingredients }
    class FeastOrder { +orderId +customerCode +menuCode +numberOfTables +eventDate +menuPrice }
```

# Program Flowchart

The program follows a layered, component-oriented design. Dependencies point
inward from the console workflow toward business services and repositories.

```mermaid
flowchart LR
    User --> Main
    Main --> FeastController
    FeastController --> ConsoleView
    FeastController --> CustomerService
    FeastController --> MenuService
    FeastController --> OrderService
    CustomerService --> CustomerRepository
    OrderService --> OrderRepository
    MenuService --> MenuRepository
    CustomerRepository --> DatFiles[(customers.dat / feast_order_service.dat)]
    OrderRepository --> DatFiles
    MenuRepository --> CsvFile[(feastMenu.csv)]
```

```mermaid
flowchart TD
    A[Start] --> B[Load customers, menus, and orders]
    B --> C[Show main menu]
    C --> D{Choice}
    D -->|1-6| E[Validate and process feature]
    E --> C
    D -->|7| F[Save customers and orders]
    F --> C
    D -->|8| G[Display sorted customer or order list]
    G --> C
    D -->|9| H{Unsaved changes?}
    H -->|No| I[End]
    H -->|Yes| J{Save or discard}
    J --> I
```

The classes are organized into the same four packages as the supplied Sample:
`Entity`, `DataObject`, `Utilities`, and `Program`.

## Place-order sequence

```mermaid
sequenceDiagram
    actor User
    participant Controller as FeastController
    participant Customers as CustomerService
    participant Menus as MenuService
    participant Orders as OrderService
    participant View as ConsoleView
    User->>Controller: Enter customer, menu, tables, date
    Controller->>Customers: findByCode(customerCode)
    Controller->>Menus: findByCode(menuCode)
    Controller->>Orders: create(validated values)
    Orders->>Orders: Reject duplicate or generate next ID
    Orders-->>Controller: FeastOrder / null
    Controller->>View: Display order or duplicate message
```
