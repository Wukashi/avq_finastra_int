# Transfer Service API (Łukasz)

This service provides three REST endpoints to handle InstantPayment XML messages.

---

## Endpoints

### 1. `/transfer_raw`
- **Method:** POST
- **Consumes:** application/xml
- **Produces:** application/xml
- **Description:**  
  Accepts an InstantPayment XML and returns the raw JAXB marshalled response without formatting or namespace modifications.

---

### 2. `/transfer_output_formatted`
- **Method:** POST
- **Consumes:** application/xml
- **Produces:** text/plain
- **Description:**  
  Accepts an InstantPayment XML and returns a pretty-printed (human-readable) XML,  
  with all namespace prefixes (e.g., `ns2:`) removed from elements.

---

### 3. `/transfer_xmlns_to_doc`
- **Method:** POST
- **Consumes:** application/xml
- **Produces:** application/xml
- **Description:**  
  Accepts an InstantPayment XML and returns XML where the `<Document>` element contains only the default namespace  
  (without removing namespace prefixes or applying pretty formatting).


# Xml Mapper Service API (Dominik)

This service provides two REST endpoints to handle InstantPayment XML messages.

---

## Endpoints

### 1. `/transfer-dom`
- **Method:** POST
- **Consumes:** application/xml
- **Produces:** application/xml
- **Description:**  
  Accepts an InstantPayment XML and returns a pretty-printed (human-readable) XML,  
  formatted using DOM, all unnecessary namespace prefixes (e.g., `ns2:`) removed from elements, missing ns declarations added.
  

---

### 2. `/transfer-ns`
- **Method:** POST
- **Consumes:** application/xml
- **Produces:** application/xml
- **Description:**  
  Accepts an InstantPayment XML and returns the JAXB marshalled response without namespace modifications.
