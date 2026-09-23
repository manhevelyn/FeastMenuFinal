# Traditional Feast Order Management

Java console application for LAB211 assignment `J1.L.P0028`.

## Requirements

- JDK 21 or newer
- Apache Ant (or NetBeans)

## Run

```text
ant run
```

The application reads menu choices from `feastMenu.csv` and stores data in:

- `customers.dat`
- `feast_order_service.dat`

Dates are entered in `dd/MM/yyyy` format. Errors are displayed directly in the console.

## Structure (following the provided Sample)

- `Entity`: domain objects
- `DataObject`: persistence and business-data operations
- `Utilities`: validation, input, and logging
- `Program`: application workflow, console output, and entry point
