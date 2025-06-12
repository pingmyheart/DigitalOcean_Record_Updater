# DigitalOcean Record Updater

*Java application to update DigitalOcean DNS records dynamically*

![Last Commit](https://img.shields.io/github/last-commit/pingmyheart/DigitalOcean-Record-Updater)
![Repo Size](https://img.shields.io/github/repo-size/pingmyheart/DigitalOcean-Record-Updater)
![Issues](https://img.shields.io/github/issues/pingmyheart/DigitalOcean-Record-Updater)
![Pull Requests](https://img.shields.io/github/issues-pr/pingmyheart/DigitalOcean-Record-Updater)
![License](https://img.shields.io/github/license/pingmyheart/DigitalOcean-Record-Updater)
![Top Language](https://img.shields.io/github/languages/top/pingmyheart/DigitalOcean-Record-Updater)
![Language Count](https://img.shields.io/github/languages/count/pingmyheart/DigitalOcean-Record-Updater)

## Why DigitalOcean Record Updater?

DigitalOcean Record Updater is a spring boot application that permit to anyone who has a DigitalOcean account with DNS
project and want to manage them dynamically depending on a specific location of a device/server where the application is
going to be deployed.  
Basically, DigitalOcean Record Updater works
with [DigitalOcean API](https://docs.digitalocean.com/reference/api/api-reference/)
to update **type A DNS records** retrieving the current public ip address and updating it to a set of domains passed to
the application through a scheduled cron settled into **application.yml** file.  
Released images are also available on [DockerHub](https://hub.docker.com/r/pingmyheart/digitalocean_record_updater)

## How it works

Taking into account that you can have a single project or multiple project managed into DigitalOcean, the application
can work in both the scenarios in fact the configuration is generic like below

```yaml
io:
  github:
    pingmyheart:
      digitaloceanrecordupdater:
        projects:
          - domain: domain.ovh
            hostnames:
              - test1
              - test2
              - test3
          - domain: domain2.ovh
            hostnames:
              - test4
              - test5
```

## Deploy into Docker

1. **Pull the Docker image**:

```bash
docker pull ghcr.io/pingmyheart/digitalocean-record-updater:${VERSION}
```

2. **Create the configuration file named `application-docker.yml`**:

```yaml
io:
  github:
    pingmyheart:
      digitaloceanrecordupdater:
        projects:
          - domain: domain.ovh
            hostnames:
              - test1
              - test2
              - test3
          - domain: domain2.ovh
            hostnames:
              - test4
              - test5
```

3. **Run the Docker container**:

```yaml
services:
  record-updater:
    image: ghcr.io/pingmyheart/digitalocean-record-updater:${VERSION}
    environment:
      - SPRING_PROFILES_ACTIVE=docker
      - BEARER_TOKEN=your_digital_ocean_api_token
    volumes:
      - ./application-docker.yml:/config/application-docker.yml
```