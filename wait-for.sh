#!/bin/bash

host=$1     # Primer argumento: nombre del servicio (db_pacientes)
port="$2"   # Segundo argumento: puerto (3306)
shift 2     # Elimina los dos primeros argumentos
cmd="$@"    # Todo lo que sigue se considera el comando a ejecutar

echo "Waiting for $host:$port..."

# Intenta conectarse usando /dev/tcp, espera 1s entre intentos
while ! timeout 1 bash -c "cat < /dev/null > /dev/tcp/$host/$port" 2>/dev/null; do
  sleep 1
done

echo "$host:$port is available, starting command..."
exec $cmd