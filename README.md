# 7th assignment for the course of Software Architecture and Platforms 2023/2024
## Description
#### Software Architecture and Platforms - a.y. 2023-2024
 
## Assignment #7 - 20231201 - Observable Microservices
- **Description:** The objective of the assignment is to apply in practice some patterns for making production-ready microservices as seen in module-2.9, using the technologies discussed in [Lab-11-20231201](https://github.com/pslab-unibo/sap-2023-2024/blob/master/Labs/Lab-11-20231201/README.md). To this purpose:

	- Consider either the Cooperative Pixel Art example (Lab-07-20231027), or, alternatively, your own example/case/project that you developed or you are developing. The example includes a simple API Gateway that interacts with a PixelArtGrid service.
	
	- Extend the example by applying the following patterns:
		- Health Check API
		- Application metrics
		- Distributed Tracing 
		- Distributed Logging
	
	- Think about metrics that can be used for defining Quality Attribute Scenarios

- **Deliverable**:  
	- Github repo including a report and the source code, organised in a proper way 

- **Remarks**
	- This is a *mandatory* assignment for those who choose to do the practical part of the exam using assignments


## How to run
```
docker compose up
```
### Show dashboard page
After all containers are healthy by connecting to [http://localhost:3000/static/ride-dashboard.html](http://localhost:3000/static/ride-dashboard.html) will show the dashboard page.

### Setup Grafana
In order to view the Prometheus metrics it is possible to connect to [http://loocalhost:3001](http://loocalhost:3001), after that a few steps have to be followed.

- login with username `admin` and password `admin`
- On the left hand side go to *Data sources*
- Create a new prometheus dashboard
  - Click *Build a dashboard*
  - *Add visualization* and select *Prometheus*
  - In the *Metric* field type *requests_total*
  - By clicking run queries the graph should populate
  - Save the dashboard

