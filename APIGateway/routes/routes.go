package routes

type Config struct {
	Routes []Route `yaml:"routes"`
}

type Route struct {
	Method string `yaml:"method"`
	Path   string `yaml:"path"`
	Target string `yaml:"target"`
}

type Routes interface {
    LoadConfig() (*Config, error)
}
