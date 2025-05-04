# Deping - Uptime Monitor

This is a hobby project for demonstration built with Spring Boot and Angular that allows users to track the uptime of their websites. It features two main entities:

- **Users:** These users can add the URLs they want to monitor. The application displays the current status (up or down) of these websites.
- **Validators:** These users contribute to the uptime monitoring process by periodically checking the status of URLs. As a reward for their contribution, validators receive Hbar.

The application uses the `Hiero-Enterprise-Java` module to handle all functionalities related to rewarding validators and managing in-app hiero account.

## Getting Started (For Development)

1.  **Prerequisites:**
    * Java Development Kit (JDK)
    * Maven
    * Node.js and npm (or bun)
    * MetaMask browser extension (Optional)

2.  **Backend Setup:**
    - Run the docker compose file to set db.
      ```sh
      docker compose up -d
      ```
    - Navigate to the `api` directory.
      ```sh
      cd api
      ```
    - In `api` directory, create a file named `.env`.
    - Add the necessary Hiero configuration
      ```properties
      HIERO_ACCOUNT_ID=<your-hedear-acount-id>
      HIERO_PRIVATE_KEY=<your-hedera-privatekey>
      ```
    - Run the application using Maven: `mvn spring-boot:run`

3.  **Frontend Setup:**
    * Navigate to the `client` directory.
      ```sh
      cd client
      ```
    * Install dependencies: 
      ```sh
      npm install
      ```
    * Start the development server: 
      ```sh
      ng serve -o
      ```
    * Open your browser and navigate to `http://localhost:4200`.
