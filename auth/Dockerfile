# syntax=docker/dockerfile:1.5.2-labs
# SPDX-FileCopyrightText: 2021 City of Oulu
#
# SPDX-License-Identifier: LGPL-2.1-or-later

FROM node:18.20.5 AS theme
COPY --from=core theme/package*.json ./
RUN npm ci
COPY --from=core theme .
COPY theme-fi.sed ./
COPY theme-en.sed ./
COPY city-logo-citizen.svg evaka/login/resources/img/EspooLogoBlue.svg
RUN sed -i -E -f theme-fi.sed evaka/login/messages/messages_fi.properties && \
    sed -i -E -f theme-en.sed evaka/login/messages/messages_en.properties
RUN npm run build && rm -r evaka/account

FROM quay.io/keycloak/keycloak:26.0.6
ADD --chmod=0644 \
    --checksum=sha256:424a3e03a17df0a2bc2b3ca749d81b04e79d59cb7aeec8876a5a3f308d0caf51 \
    https://raw.githubusercontent.com/danielmiessler/SecLists/2023.1/Passwords/xato-net-10-million-passwords-1000000.txt \
    /opt/keycloak/data/password-blacklists/default.txt
COPY --from=theme evaka /opt/keycloak/themes/evaka
RUN /opt/keycloak/bin/kc.sh build --db=postgres --features=persistent-user-sessions --http-relative-path=/auth --health-enabled=true
CMD ["start","--optimized"]
