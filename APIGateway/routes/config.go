package routes

import (
	"os"
	"gopkg.in/yaml.v3"
)

func LoadRoutesConfig(filePath string)(*Config, error) {
	data, err := os.ReadFile(filePath)
	if err != nil {
		return nil, err
	}

	var config Config
	err = yaml.Unmarshal(data, &config)
	if err != nil {
		return nil, err
	}

	return &config, nil
}
