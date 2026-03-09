FROM node:18-alpine

WORKDIR /app

# Copy dependency definition from the worker directory
COPY worker/package.json ./
RUN npm install

# Copy the worker script from the worker directory
COPY worker/hedera-worker.js ./

# Copy contracts from the contracts directory
COPY contracts ./contracts

# Create shared directory for properties
RUN mkdir -p shared

CMD ["node", "hedera-worker.js"]
