#!/bin/bash

set -e  # Exit immediately if a command exits with a non-zero status

# Default values
INCLUDE_NARYO=false

# Parse options
while getopts "e" opt; do
  case ${opt} in
    e )
      INCLUDE_NARYO=true
      ;;
    * )
      echo "Usage: $0 [-e] <broker (kafka|rabbitmq)> <database (postgresql|mongodb)> <blockchain (ethereum|hedera)>"
      exit 1
      ;;
  esac
done
shift $((OPTIND -1))

# Validate input arguments
if [[ $# -ne 3 ]]; then
  echo "Usage: $0 [-e] <broker (kafka|rabbitmq)> <database (postgresql|mongodb)> <blockchain (ethereum|hedera)>"
  exit 1
fi

BROKER=$1
DATABASE=$2
BLOCKCHAIN=$3

# Validate allowed options
VALID_BROKERS=("kafka" "rabbitmq")
VALID_DATABASES=("postgresql" "mongodb")
VALID_BLOCKCHAINS=("ethereum" "hedera")

# Exact match validation
if [[ ! " ${VALID_BROKERS[*]} " =~ (^|[[:space:]])$BROKER($|[[:space:]]) ]]; then
  echo "Error: Invalid broker. Allowed values: kafka, rabbitmq"
  exit 1
fi

if [[ ! " ${VALID_DATABASES[*]} " =~ (^|[[:space:]])$DATABASE($|[[:space:]]) ]]; then
  echo "Error: Invalid database. Allowed values: postgresql, mongodb"
  exit 1
fi

if [[ ! " ${VALID_BLOCKCHAINS[*]} " =~ (^|[[:space:]])$BLOCKCHAIN($|[[:space:]]) ]]; then
  echo "Error: Invalid blockchain network. Allowed values: ethereum, hedera"
  exit 1
fi

# Ask for confirmation to start naryo service
if [[ $INCLUDE_NARYO = false ]]; then
  read -r -p "Do you want to start the naryo service as well? (y/N): " confirm
  if [[ "$confirm" =~ ^[Yy]$ ]]; then
    INCLUDE_NARYO=true
  fi
fi

# Start the environment with docker-compose
echo "Starting environment..."
docker build -t naryo/naryo .

# Set SPRING_PROFILES_ACTIVE
SPRING_PROFILES_ACTIVE="$BROKER,$DATABASE,$BLOCKCHAIN"

# Construct docker compose command
DOCKER_COMPOSE_CMD="docker compose"

# Check if .env file exists and include it
if [[ -f ".env" ]]; then
  echo "Including .env file in docker compose command."
  DOCKER_COMPOSE_CMD+=" --env-file .env"
else
  echo ".env file not found. Proceeding without it."
fi

# Add profiles
DOCKER_COMPOSE_CMD+=" --profile $BROKER --profile $DATABASE"

if [[ "$INCLUDE_NARYO" == true ]]; then
  DOCKER_COMPOSE_CMD+=" --profile naryo"
fi

# Run docker compose with environment variable
eval "SPRING_PROFILES_ACTIVE=$SPRING_PROFILES_ACTIVE $DOCKER_COMPOSE_CMD up -d"

echo "Environment started successfully."
