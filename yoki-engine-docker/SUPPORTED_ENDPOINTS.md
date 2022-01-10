# Yoki for Docker

## Supported Endpoints
### Images
* [ ] List images - GET **/images/json**
* [ ] Build an image - POST **/build**
* [ ] Delete builder cache - POST **/build/prune**
* [ ] Create an image - POST **/images/create**
* [ ] Inspect an image - GET **/images/:name/json**
* [ ] Get the history of an image - GET **/images/:name/history**
* [ ] Push an image to a registry - POST **/images/:name/push**
* [ ] Tag an image - POST **/images/:name/tag**
* [ ] Remove an image - DELETE **/images/:name**
* [ ] Search images - GET **/images/search**
* [ ] Delete unused images - POST **/images/prune**
* [ ] Create a new image from a container - POST **/commit**
* [ ] Export and image - GET **/images/:name/get**
* [ ] Export several images - GET **/images/get**
* [ ] Import images - POST **/images/load**

### Network
* [ ] List networks - GET **/networks**
* [ ] Inspect a network - GET **/networks/:id**
* [ ] Remove a network - DELETE **/networks/:id**
* [ ] Create a network - POST **/networks/create**
* [ ] Connect a container to a network - POST **/networks/:id/connect**
* [ ] Disconnect a container from a network - POST **/networks/:id/disconnect**
* [ ] Delete unused networks - POST **/networks/prune**

### Volumes
* [x] List volumes - GET **/volumes**
* [x] Create a volume - POST **/volumes/create**
* [x] Inspect a volume - GET **/volumes/:name**
* [x] Remove a volume - DELETE **/volumes/:name**
* [x] Delete unused volumes - POST **/volumes/prune**

### Exec
* [ ] Start an exec instance - POST **/exec/:id/start**
* [ ] Resize an exec instance - POST **/exec/:id/resize**
* [ ] Inspect an exec instance - GET **/exec/:id/json**

### Swarm
* [ ] Inspect swarm - GET **/swarm**
* [ ] Initialize a new swarm - POST **/swarm/init**
* [ ] Join an existing swarm - POST **/swarm/join**
* [ ] Leave a swarm - POST **/swarm/leave**
* [ ] Update a swarm - POST **/swarm/update**
* [ ] Get swarm unlock key - GET **/swarm/unlockkey**
* [ ] Unlock a swarm locked manager - POST **/swarm/unlock**

### Nodes
* [ ] List nodes - GET **/nodes**
* [ ] Inspect a node - GET **/nodes/:id**
* [ ] Delete a node - DELETE **/nodes/:id**
* [ ] Update a node - POST **/nodes/:id/update**

### Services
* [ ] List services - GET **/services**
* [ ] Create a service - POST **/services/create**
* [ ] Inspect a service - GET **/services/:id**
* [ ] Delete a service - DELETE **/services/:id**
* [ ] Update a service - POST **/services/:id/update**
* [ ] Get service logs - GET **/services/:id/logs**

### Tasks
* [ ] List tasks - GET **/tasks**
* [ ] Inspect a task - GET **/tasks:id**
* [ ] Get task logs - GET **/tasks/:id/logs**

### Secrets
* [ ] List secrets - GET **/secrets**
* [ ] Create a secret - POST **/secrets/create**
* [ ] Inspect a secret - GET **/secrets/:id**
* [ ] Delete a secret - DELETE **/secrets/:id**
* [ ] Update a secret - POST **/secrets/:id/update**

### Configs
* [ ] List configs - GET **/configs**
* [ ] Create a config - POST **/configs/create**
* [ ] Inspect a config - GET **/configs/:id**
* [ ] Delete a config - DELETE **/configs/:id**
* [ ] Update a config - POST **/configs/:id/update**

### Plugins
* [ ] List plugins - GET **/plugins**
* [ ] Get plugin privilegies - GET **/plugins/privilegies**
* [ ] Install a plugin - POST **/plugins/pull**
* [ ] Inspect a plugin - GET **/plugins/:name/json**
* [ ] Remove a plugin - DELETE **/plugins/:name**
* [ ] Enable a plugin - POST **/plugins/:name/enable**
* [ ] Disable a plugin - POST **/plugins/:name/disable**
* [ ] Upgrade a plugin - POST **/plugins/:name/upgrade**
* [ ] Create a plugin - POST **/plugins/create**
* [ ] Push a plugin - POST **/plugins/:name/push**
* [ ] Configure a plugin - POST **/plugins/:name/set**

### System
* [ ] Check auth configuration - POST **/auth**
* [ ] Get system information - GET **/info**
* [ ] Get version - GET **/version**
* [ ] Ping - GET **/_ping**
* [ ] Ping - HEAD **/_ping**
* [ ] Monitor events - GET **/events**
* [ ] Get data usage information - GET **/system/df**

### Distribution
* [ ] Get image information from the registry - GET **/distribution/:name/json**

### Session
* [ ] Initialize interactive session - POST **/session**